package com.fires.domain.asset.adapter

import com.fires.common.constant.CurrencyType
import com.fires.common.logging.Log
import com.fires.common.util.pmap.pmap
import com.fires.domain.asset.adapter.client.YfinaceClient
import com.fires.domain.asset.adapter.dto.YfinaceRequest
import com.fires.domain.asset.application.port.out.AssetPricePort
import com.fires.domain.asset.constant.CountryType
import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.asset.domain.entity.Price
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class AssetPriceAdapter(
    private val yfinaceClient: YfinaceClient
) : AssetPricePort, Log {

    override fun getAssetsCurrentPrice(assets: List<Asset>): List<Price> = runBlocking {
        assets.pmap { getAssetCurrentPrice(it) }
    }

    override fun getAssetCurrentPrice(asset: Asset): Price {
        return getAssetPriceOnDate(asset, LocalDate.now()).firstOrNull() ?: Price.createEmptyPrice(asset)
    }

    override fun getAssetPriceOnDate(asset: Asset, localDate: LocalDate): List<Price> {
        val request = YfinaceRequest(asset, localDate)
        return when (asset.countryType) {
            CountryType.USA -> {
                yfinaceClient.getPriceByPeriod(request).toPrices(asset.assetId, CurrencyType.USD)
            }

            CountryType.KOR -> {
                yfinaceClient.getPriceByPeriod(request).toPrices(asset.assetId, CurrencyType.KRW)
            }
        }
    }

    private fun fallbackGetAssetPrice(assets: List<Asset>, t: Throwable): List<Price> {
        log.error("getAssetPrice fail!!", t)
        return emptyList()
    }
}
