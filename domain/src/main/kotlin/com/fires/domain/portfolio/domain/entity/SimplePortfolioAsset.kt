package com.fires.domain.portfolio.domain.entity

import com.fires.common.constant.CurrencyType
import com.fires.common.util.BigDecimalUtil
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(name = "심플 포트폴리오 자산")
class SimplePortfolioAsset(
    @Schema(title = "포트폴리오 자산 아이디")
    val portfolioAssetId: Long,
    @Schema(title = "구매수량")
    val count: Double,
    @Schema(title = "구매 가격")
    var purchasePrice: BigDecimal,
    @Schema(title = "통화타입")
    var currencyType: CurrencyType,
    @Schema(title = "자산 아이디")
    val assetId: Long,
    @Schema(title = "자산 순서")
    val priority: Int
) {
    fun changeToTargetCurrencyType(targetCurrencyType: CurrencyType, exchangeRate: Double) {
        if(targetCurrencyType == this.currencyType) return

        this.currencyType = targetCurrencyType
        val usdExchangeRate = exchangeRate.toBigDecimal()
        when(targetCurrencyType){
            // 미화 -> 원화
            CurrencyType.KRW ->
                this.purchasePrice = this.purchasePrice.multiply(usdExchangeRate)

            // 원화 -> 미화
            CurrencyType.USD ->
                this.purchasePrice = BigDecimalUtil.divideRoundToTwoDecimal(this.purchasePrice, usdExchangeRate)

            else -> {}
        }
    }

    fun getTotalPrice(): BigDecimal = purchasePrice.multiply(count.toBigDecimal())

    fun getTotalPurchasePrice(): BigDecimal {
        return this.purchasePrice * this.count.toBigDecimal()
    }
}
