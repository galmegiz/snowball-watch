package com.fires.domain.portfolio.domain.dto

import com.fires.common.constant.CurrencyType
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.math.RoundingMode

@Schema(name = "보유 자산 정보 상세")
data class SimplePortfolioAssetResponse(
    @Schema(title = "포트폴리오 자산 아이디")
    val portfolioAssetId: Long,
    @Schema(title = "자산 아이디")
    val assetId: Long,
    @Schema(title = "구매수량")
    val count: Double,
    @Schema(title = "구매 가격")
    var purchasePrice: BigDecimal,
    @Schema(title = "통화타입")
    val currencyType: CurrencyType,
    @Schema(title = "티커코드")
    val tickerCode: String? = null,
    @Schema(title = "종목코드")
    val stockCode: String? = null,
    @Schema(title = "자산명")
    val name: String,
    @Schema(title = "우선순위")
    val order: Int = 100
){
    init{
        this.purchasePrice = this.purchasePrice.setScale(2, RoundingMode.HALF_UP)
    }
}
