package com.fires.domain.portfolio.adapter.entity

import com.fires.common.entity.BaseEntity
import com.fires.domain.portfolio.domain.entity.Portfolio
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "portfolios")
class PortfolioEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id", nullable = false)
    val id: Long? = null,
    /**
     * 포트폴리오 소유자
     */
    val userId: Long,
    /**
     * 향후 포트폴리오 여러 개 가질 수 있으므로 우선순위 지정
     */
    val priority: Long
) : BaseEntity() {
    @OneToMany(mappedBy = "portfolioEntity", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var portfolioAssetEntityList: MutableList<PortfolioAssetEntity> = mutableListOf()

    fun addPortfolioAssetEntity(entity: PortfolioAssetEntity) {
        this.portfolioAssetEntityList.add(entity)
        entity.portfolioEntity = this
    }

    fun addPortfolioAssetEntityList(entityList: List<PortfolioAssetEntity>) {
        this.portfolioAssetEntityList.addAll(entityList)
        entityList.forEach { it.portfolioEntity = this }
    }

    fun toPortfolio() = Portfolio(
        id = this.id ?: 0,
        userId = userId,
        priority = this.priority,
        assetList = this.portfolioAssetEntityList.map { it.toSimplePortfolioAsset() }
    )
}
