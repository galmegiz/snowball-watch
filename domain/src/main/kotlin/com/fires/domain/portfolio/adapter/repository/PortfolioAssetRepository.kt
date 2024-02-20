package com.fires.domain.portfolio.adapter.repository

import com.fires.domain.portfolio.adapter.entity.PortfolioAssetEntity
import com.fires.domain.portfolio.adapter.entity.PortfolioEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PortfolioAssetRepository : JpaRepository<PortfolioAssetEntity, Long> {
    fun findByPortfolioEntity(defaultPortfolio: PortfolioEntity): List<PortfolioAssetEntity>
    fun findTop1ByPortfolioEntityOrderByPriorityDesc(portfolioEntity: PortfolioEntity): PortfolioAssetEntity
}
