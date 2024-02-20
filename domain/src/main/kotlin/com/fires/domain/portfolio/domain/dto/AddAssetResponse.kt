package com.fires.domain.portfolio.domain.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "포트폴리오 자산 추가 응답")
data class AddAssetResponse(
    @Schema(title = "포트폴리오 id")
    val portfolioId: Long,
    @Schema(title = "추가된 자산 리스트")
    val assets: List<AssetInPortfolio>
) {
    @Schema(name = "포트폴리오에 추가된 자산정보")
    data class AssetInPortfolio(
        @Schema(title = "자산 id")
        val assetId: Long,
        @Schema(title = "자산 id")
        val assetPrice: String,
        @Schema(title = "자산 수량")
        val assetCount: Long,
        @Schema(title = "자산 우선순위")
        val priority: Long
    )
}
