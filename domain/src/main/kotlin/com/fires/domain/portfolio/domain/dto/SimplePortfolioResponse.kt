package com.fires.domain.portfolio.domain.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.math.RoundingMode

@Schema(name = "보유 자산 정보 요약")
data class SimplePortfolioResponse(
    @Schema(title = "포트폴리오 id")
    val portfolioId: Long,
    @Schema(title = "총 자산 가격")
    var totalValue: BigDecimal,
    @Schema(title = "총 자산 등락")
    var totalValueChange: BigDecimal,
    @Schema(title = "총 자산 등락율")
    var totalValueChangeRate: BigDecimal,
    @Schema(title = "개별 자산 정보")
    val assetDetails: MutableList<DetailPortfolioAssetResponse>

){
    init {
        this.totalValue = this.totalValue.setScale(2, RoundingMode.HALF_UP)
        this.totalValueChange = this.totalValueChange.setScale(2, RoundingMode.HALF_UP)
        this.totalValueChangeRate = this.totalValueChangeRate.setScale(2, RoundingMode.HALF_UP)
        // assetDetails.sortBy { it.order }
    }
}
