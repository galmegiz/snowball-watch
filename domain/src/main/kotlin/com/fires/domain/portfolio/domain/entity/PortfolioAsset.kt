package com.fires.domain.portfolio.domain.entity

import com.fires.common.constant.CurrencyType
import com.fires.domain.asset.domain.entity.Asset
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(name = "포트폴리오 자산")
data class PortfolioAsset(
    @Schema(title = "포트폴리오 자산 아이디")
    val id: Long,
    @Schema(title = "구매수량")
    val count: Double,
    @Schema(title = "구매 가격")
    val purchasePrice: BigDecimal,
    @Schema(title = "통화타입")
    val currencyType: CurrencyType,
    @Schema(title = "자산")
    val asset: Asset?
){
    fun getTotalPrice(): BigDecimal = purchasePrice.multiply(count.toBigDecimal())
}
