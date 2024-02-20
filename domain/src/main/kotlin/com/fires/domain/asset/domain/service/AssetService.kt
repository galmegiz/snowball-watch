package com.fires.domain.asset.domain.service

import com.fires.common.constant.CacheConstant
import com.fires.common.constant.CurrencyType
import com.fires.common.logging.Log
import com.fires.common.util.pmap.pEach
import com.fires.domain.asset.application.port.`in`.AssetQueryCommand
import com.fires.domain.asset.application.port.`in`.AssetUseCase
import com.fires.domain.asset.application.port.out.AssetPort
import com.fires.domain.asset.application.port.out.AssetPricePort
import com.fires.domain.asset.constant.AssetMarketType
import com.fires.domain.asset.domain.dto.CurrentPriceRequest
import com.fires.domain.asset.domain.dto.DailyClosePrice
import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.asset.domain.entity.Price
import com.fires.domain.exchangerate.application.port.out.ExchangeRatePort
import com.fires.domain.user.application.port.out.RecentSearchWordPort
import kotlinx.coroutines.runBlocking
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Service
@Transactional
class AssetService(
    private val assetPort: AssetPort,
    private val assetPricePort: AssetPricePort,
    private val recentSearchWordPort: RecentSearchWordPort,
    private val exchangeRatePort: ExchangeRatePort,
    private val cacheManager: CacheManager
) : Log, AssetUseCase {
    @Transactional(readOnly = true)
    override fun findAsset(request: AssetQueryCommand, email: String): Slice<Asset> {
        val pageIndex = when {
            request.pageIndex > 1 -> request.pageIndex - 1
            else -> 0
        }
        val pageRequest = PageRequest.of(pageIndex, request.pageSize)
        return assetPort.queryAsset(request.word, pageRequest)
    }

    @Transactional(readOnly = true)
    override fun findAssetsInIds(assetIds: Collection<Long>): List<Asset> = assetPort.findAssetList(assetIds)

    override fun updateAssetMarketType(assetId: Long, marketType: AssetMarketType) {
        assetPort.updateAssetMarketType(assetId, marketType)
    }

    @Transactional(readOnly = true)
    override fun getAssetsCurrentPrice(currentPriceRequest: List<CurrentPriceRequest>): List<Price> = runBlocking{
        val requestMap: Map<Long, CurrentPriceRequest> = currentPriceRequest.associateBy { it.assetId }
        val assetMap: MutableMap<Long, Asset> = findAssetsInIds(requestMap.keys).associateBy { it.assetId }.toMutableMap()
        val priceCache  = cacheManager.getCache(CacheConstant.PRICE)

        // val pricesInCache = getPricesFromCache(requestMap, priceCache)

       // val assetsForApiRequest = (assetMap - pricesInCache.map { it.assetId }.toSet()).values
        val priceFromApi = assetPricePort.getAssetsCurrentPrice(assetMap.values.toList())

        val prices = priceFromApi
        val usdExchangeRate = exchangeRatePort.getUsdExchangeRate().value

        prices.pEach {
            val targetCurrencyType = requestMap[it.assetId]?.currencyType ?: return@pEach
            it.changeToTargetCurrencyType(targetCurrencyType, usdExchangeRate)
        }

        // saveInCache(prices, priceCache)
        return@runBlocking prices
    }

    private fun saveInCache(
        prices: List<Price>,
        priceCache: Cache?
    ) {
        prices.forEach {
            val cacheKey = it.assetId.toString() + it.currencyType
            priceCache?.put(cacheKey, it)
        }
    }

    private fun getPricesFromCache(
        requestMap: Map<Long, CurrentPriceRequest>,
        priceCache: Cache?
    ): List<Price> {
        val prices = mutableListOf<Price>()
        for (key in requestMap.keys) {
            val cacheKey = key.toString() + requestMap[key]?.currencyType.toString()
            val price = priceCache?.get(cacheKey, Price::class.java) ?: continue
            prices.add(price)
        }
        return prices
    }

    @Transactional(readOnly = true)
    // @Cacheable(cacheNames = [CacheConstant.PRICE_WITH_CHANGE], key = "#asset.assetId")
    override fun getCurrentPriceAndPriceChange(asset: Asset): Price {
        val prices = assetPricePort.getAssetPriceOnDate(asset, LocalDate.now().minusDays(2L))
        if (prices.isEmpty()) {
            return Price(asset.assetId,
                BigDecimal.ZERO,
                CurrencyType.KRW,
                LocalDateTime.now(),
                "0",
                BigDecimal.ZERO,
                BigDecimal.ZERO)
        }

        val lastPriceIndex = prices.lastIndex
        val currentPrice = prices[lastPriceIndex]

        val prevPrice = if (lastPriceIndex < 1) {
            currentPrice
        } else {
            prices[lastPriceIndex - 1]
        }
        val prevClosPrice = DailyClosePrice(asset.assetId, prevPrice.currentPrice,  prevPrice.currencyType, prevPrice.accessTime, prevPrice.accessTime.toString())


        currentPrice.updatePriceChange(prevClosPrice)
        return currentPrice
    }

}
