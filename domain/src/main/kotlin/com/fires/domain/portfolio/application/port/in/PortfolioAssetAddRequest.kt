package com.fires.domain.portfolio.application.port.`in`

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "포트폴리오 내 자산 추가")
data class PortfolioAssetAddRequest(
    @Schema(title = "포트폴리오 id")
    val portfolioId: Long,
    @Schema(title = "자산 추가 리스트")
    val assets: List<AssetAddRequest>
)
