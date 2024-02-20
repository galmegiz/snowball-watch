package com.fires.domain.portfolio.domain

import com.fires.common.constant.CurrencyType
import com.fires.common.exception.ErrorCode
import com.fires.common.exception.ServiceException
import com.fires.common.logging.Log
import com.fires.common.util.BigDecimalUtil
import com.fires.common.util.pmap.pEach
import com.fires.common.util.pmap.pmap
import com.fires.domain.asset.application.port.`in`.AssetUseCase
import com.fires.domain.asset.application.port.out.AssetPort
import com.fires.domain.asset.application.port.out.AssetPricePort
import com.fires.domain.asset.constant.CountryType
import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.asset.domain.entity.SimpleAssetDividend
import com.fires.domain.dividends.application.port.out.DividendApiPort
import com.fires.domain.dividends.domain.entity.Dividend
import com.fires.domain.exchangerate.application.port.out.ExchangeRatePort
import com.fires.domain.portfolio.application.port.`in`.PortfolioReadUseCase
import com.fires.domain.portfolio.application.port.out.PortfolioPort
import com.fires.domain.portfolio.domain.dto.DetailPortfolioAssetResponse
import com.fires.domain.portfolio.domain.dto.PortfolioAssetInfoRequest
import com.fires.domain.portfolio.domain.dto.SimplePortfolioAssetResponse
import com.fires.domain.portfolio.domain.dto.SimplePortfolioResponse
import com.fires.domain.portfolio.domain.entity.SimplePortfolioAsset
import jakarta.transaction.Transactional
import kotlinx.coroutines.runBlocking
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.YearMonth

@Service
@Transactional
class PortfolioReadService(
    private val portfolioPort: PortfolioPort,
    private val assetPort: AssetPort,
    private val assetPricePort: AssetPricePort,
    private val dividendPort: DividendApiPort,
    private val exchangeRatePort: ExchangeRatePort,
    private val assetUseCase: AssetUseCase,
    private val cacheManager: CacheManager
) : PortfolioReadUseCase, Log {

    override fun getPortfolioWithSimpleAsset(userId: Long): SimplePortfolioResponse = runBlocking{
        val portfolio = portfolioPort.getDefaultPortfolio(userId)
                                            .withValidUserCheck(userId)

        // TODO fetch join으로 해결 가능
        val assetIds = portfolio.assetList.map { it.assetId }
        val portfolioAssetMap: Map<Long, SimplePortfolioAsset> =
            portfolio.assetList.associateBy { it.assetId }
        val assetMap: Map<Long, Asset> =
            assetPort.findAssetList(assetIds).associateBy { it.assetId }

        // 자산별 배당금 DB 정보
        // Todo 자산별 배당정보는 배치나 스케쥴잡을 통해서 1일 1회 동기화 진행
        // Todo 동기화 로직 구현 시 위 로직으로 대체(현재는 프론트 연동 테스트를 위해 아래 코드 유지)
        val usdExchangeRate: Double = exchangeRatePort.getUsdExchangeRate().value
        val dividendsMap: Map<Long, List<SimpleAssetDividend>>
            = getKRWDividendsFromExternalApi(assetMap.values, usdExchangeRate)
        val totalValueCalculator = TotalAssetValueCalculator(usdExchangeRate)

        // 자산별 배당정보를 순회하며 필요한 값을 계산
        val assetDetails = dividendsMap.entries.pmap {
            val assetId = it.key
            val asset = assetMap[assetId] ?: throw ServiceException(ErrorCode.ASSET_NOT_FOUND)

            val portfolioAsset = portfolioAssetMap[assetId] ?: throw ServiceException(ErrorCode.ASSET_NOT_FOUND)
            portfolioAsset.changeToTargetCurrencyType(CurrencyType.KRW, usdExchangeRate)

            val price = assetUseCase.getCurrentPriceAndPriceChange(asset)
            price.changeToTargetCurrencyType(CurrencyType.KRW, usdExchangeRate)

            val currentPrice = price.currentPrice
            val purchasePrice = portfolioAsset.purchasePrice
            val count = portfolioAsset.count.toBigDecimal()

            val dividends = it.value
            // TODO 배당 관련 entity 생성
            // 투자배당률
            val dividendPriceRatio =
                calculateDividendPriceRatio(
                    it.value.map { value -> value.dividend },
                    purchasePrice
                )

            val dividendMonth = it.value.map { data -> data.payDate?.month?.value ?: 0 }.toSortedSet()

            // 수익률 = (현재 주식 가격 / 매수한 주식 가격) * 100 - 100
            val rateOfReturn = BigDecimalUtil.calculatePercentChange(currentPrice, purchasePrice) - BigDecimal("100")
            // 자산 가치 =  현재가 * 자산 보유 수
            val assetValue = currentPrice.multiply(count)

            totalValueCalculator.addAssetPriceAndValue(price, portfolioAsset)

            return@pmap DetailPortfolioAssetResponse(
                asset = asset,
                portfolioAsset = portfolioAsset,
                price = price,
                assetValue = assetValue,
                rateOfReturn = rateOfReturn,
                dividendPriceRatio = dividendPriceRatio,
                dividendMonth = dividendMonth
            )
        }


        return@runBlocking SimplePortfolioResponse(
            portfolioId = portfolio.id,
            totalValue = totalValueCalculator.totalValue,
            totalValueChange = totalValueCalculator.getTotalValueChangeRate(),
            totalValueChangeRate = totalValueCalculator.getTotalValueChangeRate(),
            assetDetails = assetDetails.toMutableList()
        )
    }

    // TODO 처리과정 비효율적임
    override fun getPortfolioAssetInfo(request: PortfolioAssetInfoRequest, userId: Long): SimplePortfolioAssetResponse {
        val portfolio = portfolioPort.getPortfolio(request.portfolioId).withValidUserCheck(userId)
        val portfolioAssetEntity = portfolioPort.getPortfolioAsset(request.portfolioAssetId)
        val asset = assetPort.findAsset(portfolioAssetEntity.assetId)
        return portfolioAssetEntity.toPortfolioAssetResponse(asset)
    }

    /**
     * @param purchasePrice : 자산 평단가
     */
    private fun calculateDividendPriceRatio(dividendList: List<BigDecimal>, purchasePrice: BigDecimal): BigDecimal {
        if (dividendList.isEmpty() || purchasePrice.equals(0)) {
            return BigDecimal.ZERO
        }

        val averageDividend = dividendList.fold(BigDecimal.ZERO, BigDecimal::add)
        return BigDecimalUtil.calculatePercentChange(averageDividend, purchasePrice)
    }

    private suspend fun getKRWDividendsFromExternalApi(
        assets: Collection<Asset>,
        usdExchangeRate: Double
    ): Map<Long, List<SimpleAssetDividend>> {
        val result = mutableMapOf<Long, List<SimpleAssetDividend>>()
        assets.pEach { asset ->

            val exchangeRate = when (asset.countryType) {
                CountryType.USA -> usdExchangeRate
                CountryType.KOR -> 1.0
            }
            val dividends: List<Dividend> =
                dividendPort.queryDividends(
                    asset = asset,
                    from = YearMonth.now().minusYears(1L)
                )

            val simpleDividends: List<SimpleAssetDividend> =
                dividends.map { dividend ->
                    SimpleAssetDividend.fromDividend(dividend, exchangeRate)
                }

            result[asset.assetId] = simpleDividends
        }

        return result
    }
}
