package com.fires.domain.dividends.domain

import com.fires.common.constant.CurrencyType
import com.fires.common.exception.ErrorCode
import com.fires.common.exception.ServiceException
import com.fires.common.logging.Log
import com.fires.common.util.BigDecimalUtil
import com.fires.common.util.pmap.pEach
import com.fires.domain.asset.application.port.`in`.AssetUseCase
import com.fires.domain.asset.application.port.out.AssetPort
import com.fires.domain.asset.application.port.out.AssetPricePort
import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.asset.domain.entity.Price
import com.fires.domain.dividends.application.port.`in`.DividendUseCase
import com.fires.domain.dividends.application.port.out.DividendApiPort
import com.fires.domain.dividends.domain.dto.AnnualDividendResponse
import com.fires.domain.dividends.domain.dto.DividendTaxCalculator
import com.fires.domain.dividends.domain.dto.ThisMonthDividendResponse
import com.fires.domain.dividends.domain.entity.Dividend
import com.fires.domain.exchangerate.application.port.out.ExchangeRatePort
import com.fires.domain.portfolio.application.port.out.PortfolioPort
import com.fires.domain.portfolio.domain.entity.Portfolio
import com.fires.domain.portfolio.domain.entity.SimplePortfolioAsset
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Month
import java.time.YearMonth

@Component
class DividendService(
    private val portfolioPort: PortfolioPort,
    private val assetPort: AssetPort,
    private val dividendApiPort: DividendApiPort,
    private val assetPricePort: AssetPricePort,
    private val exchangeRatePort: ExchangeRatePort,
    private val assetUseCase: AssetUseCase
) : DividendUseCase, Log {

    override fun getDividendCalendar(userId: Long): List<ThisMonthDividendResponse> = runBlocking{
        val defaultPortfolio: Portfolio = portfolioPort.getDefaultPortfolio(userId)
                                                            .withValidUserCheck(userId)
        val portfolioAssets: Map<Long, SimplePortfolioAsset> = defaultPortfolio.assetList.associateBy { it.assetId }
        val assets: Map<Long, Asset> = assetPort.findAssetList(portfolioAssets.keys.toList()).associateBy { it.assetId }
        // TODO 배당정보 주기적 업데이트 기능 구현 시 사용
        // val assetDividends: Map<Long, List<SimpleAssetDividend>> = assetPort.getAssetDividendsThisMonth(assets.keys)
        val result = mutableListOf<ThisMonthDividendResponse>()
        val todayExchangeRate = exchangeRatePort.getUsdExchangeRate().value

        portfolioAssets.entries.pEach { entry ->
            val asset: Asset = assets[entry.key] ?: throw ServiceException(ErrorCode.ASSET_NOT_FOUND)
            val userAsset: SimplePortfolioAsset = entry.value

            val dividend: Dividend = dividendApiPort.queryDividendsThisMonth(
                asset = asset,
                YearMonth.now().minusYears(1L)
            ).firstOrNull() ?: return@pEach

            dividend.toKRW(asset, todayExchangeRate)
            val expectedDividends = dividend.getTotalDividendCashAMount(userAsset)

            val response = ThisMonthDividendResponse(
                asset = asset,
                dividend = dividend,
                expectedDividends = expectedDividends
            )
            result.add(response)
        }

        return@runBlocking result
    }

    override fun getAnnualDividendInfo(userId: Long): AnnualDividendResponse = runBlocking{
        val defaultPortfolio: Portfolio = portfolioPort.getDefaultPortfolio(userId)
                                                            .withValidUserCheck(userId)
        val assetIds = defaultPortfolio.assetList.map { it.assetId }
        val portfolioAssetMap: Map<Long, SimplePortfolioAsset> = defaultPortfolio.assetList.associateBy { it.assetId }
        val assetMap: Map<Long, Asset> =
                        assetPort.findAssetList(assetIds).associateBy { it.assetId }
        val todayExchangeRate = exchangeRatePort.getUsdExchangeRate().value

        var totalPrice = BigDecimal.ZERO
        var totalDividend = BigDecimal.ZERO // 연간 총 배당금
        var monthlyDividend = mutableMapOf<Month, BigDecimal>().withDefault { BigDecimal.ZERO } // 월별 배당금
        var thisMonthDividend = BigDecimal.ZERO // 이번달 배당금
        var totalCurrentPrice = BigDecimal.ZERO // 전체 자산의 현재가
        var totalAssetDividendRatio = BigDecimal.ZERO
        var totalAssetDividendYieldRatio = BigDecimal.ZERO
        val dividendTaxCalculator = DividendTaxCalculator()

        val annulDividendCalculator = AnnualDividendCalculator()

        // TODO 병렬처리 로직으로 변경
        portfolioAssetMap.entries.pEach { entry ->
            var assetAnnualDividends = BigDecimal.ZERO

            val asset: Asset = assetMap[entry.key] ?: throw ServiceException(ErrorCode.ASSET_NOT_FOUND)
            val userAsset: SimplePortfolioAsset = entry.value
            userAsset.changeToTargetCurrencyType(CurrencyType.KRW, todayExchangeRate)

            val price: Price = assetPricePort.getAssetCurrentPrice(asset)
            price.changeToTargetCurrencyType(CurrencyType.KRW, todayExchangeRate)

            annulDividendCalculator.increaseAssetAmount()
            annulDividendCalculator.addPurchasePrice(userAsset)
            annulDividendCalculator.addCurrentPrice(price)

            val dividends = dividendApiPort.queryDividends(
                asset = asset,
                YearMonth.now().minusYears(1L)
            )

            for (dividend in dividends) {
                // 월 배당금 * 보유수량 * 환율
                dividend.toKRW(asset, todayExchangeRate)
                val cashAmountWithExchangeRate = dividend.getTotalDividendCashAMount(userAsset)
                assetAnnualDividends += cashAmountWithExchangeRate

                annulDividendCalculator.addDividendInfo(dividend, userAsset)
                dividendTaxCalculator.calculateTax(dividend, cashAmountWithExchangeRate)
            }

            val assetDividendPriceRatio = BigDecimalUtil.calculatePercentChange(assetAnnualDividends, userAsset.getTotalPurchasePrice())
            val assetDividendYieldRatio = BigDecimalUtil.calculatePercentChange(assetAnnualDividends, price.getCurrentValue(userAsset.count))
            annulDividendCalculator.addAssetDividendRatio(assetDividendPriceRatio)
            annulDividendCalculator.addAssetDividendYieldRatio(assetDividendYieldRatio)
        }

        return@runBlocking AnnualDividendResponse(
            annualAssetCalculator = annulDividendCalculator,
            dividendTaxCalculator = dividendTaxCalculator
        )
    }
}
