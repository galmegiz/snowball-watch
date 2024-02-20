package com.fires.domain.portfolio.domain

import com.fires.common.util.BigDecimalUtil
import com.fires.domain.asset.domain.entity.Price
import com.fires.domain.portfolio.domain.entity.SimplePortfolioAsset
import java.math.BigDecimal

class TotalAssetValueCalculator(
    val usdExchangeRate: Double
) {
    // 전체 자산 가치 = 자산 수량 * 자산 가격
    var totalValue: BigDecimal = BigDecimal.ZERO
        private set
    // 전체 자산 가격
    private var totalPrice: BigDecimal = BigDecimal.ZERO

    fun addAssetPriceAndValue(currentPrice: Price, portfolioAsset: SimplePortfolioAsset) {
        totalValue += currentPrice.currentPrice.multiply(portfolioAsset.count.toBigDecimal())
        totalPrice += portfolioAsset.getTotalPrice()
    }

    fun getTotalValueChange(): BigDecimal =
        totalValue.subtract(totalPrice)

    fun getTotalValueChangeRate(): BigDecimal{
        if(totalPrice.compareTo(BigDecimal.ZERO) == 0)
            return BigDecimal.ZERO

        // 자산 수익률 = (전체 자산 가격 / 전체 자산 구매가) * 100 - 100
        return BigDecimalUtil.calculatePercentChange(totalValue, totalPrice) - BigDecimalUtil.ONE_HUNDRED
    }
}