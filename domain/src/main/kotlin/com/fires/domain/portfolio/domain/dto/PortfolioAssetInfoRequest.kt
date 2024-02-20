package com.fires.domain.portfolio.domain.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "포트폴리오 저장 자산 정보 요청")
data class PortfolioAssetInfoRequest(
    @Schema(title = "포트폴리오 id")
    val portfolioId: Long,
    @Schema(title = "포트폴리오 자산 id")
    val portfolioAssetId: Long
)