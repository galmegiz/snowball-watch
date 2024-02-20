package com.fires.domain.portfolio.domain.dto

import com.fires.domain.portfolio.domain.entity.Portfolio
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "포트폴리오 생성결과")
data class PortfolioCreateInfo(
    @Schema(name = "포트폴리오 id")
    val portfolioId: Long,
    @Schema(name = "사용자 id")
    val userId: Long
) {
    companion object {
        fun from(portfolio: Portfolio): PortfolioCreateInfo {
            return PortfolioCreateInfo(
                portfolioId = portfolio.id,
                userId = portfolio.userId
            )
        }
    }
}
