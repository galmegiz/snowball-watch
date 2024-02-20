package com.fires.domain.dividends.domain

import com.fires.common.util.BigDecimalUtil
import com.fires.domain.asset.domain.entity.Price
import com.fires.domain.dividends.domain.entity.Dividend
import com.fires.domain.portfolio.domain.entity.SimplePortfolioAsset
import java.math.BigDecimal
import java.time.Month
import java.time.YearMonth

class AnnualDividendCalculator {
    // 자산 구매 가격
    private var totalPurchasePrice: BigDecimal = BigDecimal.ZERO
    // 전체 자산의 현재 가격
    private var totalCurrentPrice: BigDecimal = BigDecimal.ZERO
    // 월별 배당금
    val monthlyDividend = mutableMapOf<Month, BigDecimal>().withDefault { BigDecimal.ZERO }
    // 이번달 배당금
    var thisMonthDividend: BigDecimal = BigDecimal.ZERO
        private set
    // 전체 배당금
    var totalDividend: BigDecimal = BigDecimal.ZERO
        private set
    // 투자배당률의 총합
    // 투자배당률 (1년 전체 배당금 / 전체 주식 투자금) * 100 -> 과거
    // 투자배당률 (투자배당률의 합 / 주식 개수) -> 현재
    private var totalAssetDividendRatio = BigDecimal.ZERO
    // 시가배당률의 총합
    // (1년 전체 배당금 / 현재주식 가격) * 100 -> 과거
    // 시가배당률 (시가배당률의 합 / 주식 개수) -> 현재
    private var totalAssetDividendYieldRatio = BigDecimal.ZERO
    // 자산 개수
    private var assetAmount = 0

    fun increaseAssetAmount() {
        assetAmount++
    }

    fun addPurchasePrice(asset: SimplePortfolioAsset) {
        totalPurchasePrice += asset.purchasePrice
    }

    fun addCurrentPrice(price: Price) {
        totalCurrentPrice += price.currentPrice
    }

    fun addDividendInfo(dividend: Dividend, userAsset: SimplePortfolioAsset) {
        val cashAmountWithExchangeRate = dividend.getTotalDividendCashAMount(userAsset)

        totalDividend += cashAmountWithExchangeRate

        val parsedPayMonth = dividend.payDate.month
        monthlyDividend[parsedPayMonth] = monthlyDividend.getValue(parsedPayMonth) + cashAmountWithExchangeRate
        if (parsedPayMonth == YearMonth.now().month) thisMonthDividend += cashAmountWithExchangeRate
    }

    fun addAssetDividendRatio(assetDividendRatio: BigDecimal) {
        totalAssetDividendRatio += assetDividendRatio
    }

    fun addAssetDividendYieldRatio(assetYieldDividendRatio: BigDecimal) {
        totalAssetDividendYieldRatio += assetYieldDividendRatio
    }

    fun getDividendChange(): BigDecimal {
        val thisMonth = YearMonth.now().month
        val prevMonth = YearMonth.now().minusMonths(1L).month

        return (monthlyDividend.getValue(thisMonth) - monthlyDividend.getValue(prevMonth))
    }

    //투자 배당률 평균
    fun getAverageDividendPriceRatio(): BigDecimal {
        return BigDecimalUtil.divideRoundToTwoDecimal(totalAssetDividendRatio, assetAmount.toBigDecimal())
    }

    // 시가배당률 평균
    fun getAverageDividendYieldRatio(): BigDecimal {
        return BigDecimalUtil.divideRoundToTwoDecimal(totalAssetDividendYieldRatio, assetAmount.toBigDecimal())
    }

}