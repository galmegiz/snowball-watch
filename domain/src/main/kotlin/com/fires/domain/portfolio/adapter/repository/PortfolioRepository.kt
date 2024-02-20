package com.fires.domain.portfolio.adapter.repository

import com.fires.domain.portfolio.adapter.entity.PortfolioEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface PortfolioRepository : JpaRepository<PortfolioEntity, Long> {
    fun countByUserId(userId: Long): Long
    @EntityGraph(attributePaths = ["portfolioAssetEntityList"])
    fun findTop1ByUserIdOrderByPriorityAsc(userId: Long): PortfolioEntity?
}
