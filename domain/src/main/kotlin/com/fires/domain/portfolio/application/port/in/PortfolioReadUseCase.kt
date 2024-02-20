package com.fires.domain.portfolio.application.port.`in`

import com.fires.domain.portfolio.domain.dto.PortfolioAssetInfoRequest
import com.fires.domain.portfolio.domain.dto.SimplePortfolioAssetResponse
import com.fires.domain.portfolio.domain.dto.SimplePortfolioResponse

interface PortfolioReadUseCase {
    fun getPortfolioWithSimpleAsset(userId: Long): SimplePortfolioResponse
    fun getPortfolioAssetInfo(request: PortfolioAssetInfoRequest, id: Long): SimplePortfolioAssetResponse
}
