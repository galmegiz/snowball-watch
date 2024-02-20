package com.fires.domain.portfolio.application.port.out

import com.fires.domain.portfolio.adapter.entity.PortfolioAssetEntity
import com.fires.domain.portfolio.domain.entity.Portfolio
import com.fires.domain.portfolio.domain.entity.SimplePortfolioAsset

interface PortfolioPort {
    fun createPortfolio(userId: Long): Portfolio
    fun getPortfolio(portfolioId: Long): Portfolio
    fun getDefaultPortfolio(userId: Long): Portfolio
    fun countPortfolioOfUser(userId: Long): Long
    fun addAssets(portfolioId: Long, assets: List<PortfolioAssetAddCommand>): List<PortfolioAssetEntity>
    fun updateAssets(portfolioId: Long, assets: List<PortfolioAssetUpdateCommand>): List<PortfolioAssetEntity>
    fun deletePortfolio(portfolioId: Long)
    fun deleteAsset(portfolioAssetId: Long)
    fun isPortfolioExist(portfolioId: Long): Boolean
    fun isPortfolioAssetExist(portfolioAssetId: Long): Boolean
    fun getPortfolioAsset(portfolioAssetId: Long): PortfolioAssetEntity
    fun getPortfolioAssetByPortfolioId(portfolioId: Long): List<SimplePortfolioAsset>
}
