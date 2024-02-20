package com.fires.domain.portfolio.application.port.`in`

import com.fires.domain.portfolio.domain.dto.PortfolioAssetDeleteRequest
import com.fires.domain.portfolio.domain.dto.PortfolioCreateInfo
import com.fires.domain.portfolio.domain.entity.SimplePortfolioAsset

interface PortfolioManageUseCase {
    fun createPortfolio(userId: Long): PortfolioCreateInfo
    fun deletePortfolio(portfolioId: Long, userId: Long): Boolean
    fun addAssets(request: PortfolioAssetAddRequest, userId: Long): List<SimplePortfolioAsset>
    fun updateAssets(request: PortfolioAssetUpdateRequest, userId: Long): List<SimplePortfolioAsset>
    fun deleteAsset(request: PortfolioAssetDeleteRequest, userId: Long): Boolean
}