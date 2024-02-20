package com.fires.domain.portfolio.domain.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.jetbrains.annotations.NotNull

@Schema(title = "포트폴리오 자산 삭제 요청")
data class PortfolioAssetDeleteRequest(
    @Schema(title = "포트폴리오 Id")
    @NotNull
    val portfolioId: Long,

    @Schema(title = "포트폴리오 자산 Id")
    @NotNull
    val portfolioAssetId: Long
)
