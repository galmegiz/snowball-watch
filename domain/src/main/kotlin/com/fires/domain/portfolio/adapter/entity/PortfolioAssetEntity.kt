package com.fires.domain.portfolio.adapter.entity

import com.fires.common.constant.CurrencyType
import com.fires.common.entity.BaseEntity
import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.portfolio.domain.dto.SimplePortfolioAssetResponse
import com.fires.domain.portfolio.domain.entity.PortfolioAsset
import com.fires.domain.portfolio.domain.entity.SimplePortfolioAsset
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "portfolio_assets")
class PortfolioAssetEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_asset_id", nullable = false)
    val id: Long? = null,

    var count: Double,

    @Column(precision = 10, scale = 2)
    var purchasePrice: BigDecimal,

    @Enumerated(EnumType.STRING)
    var currencyType: CurrencyType,

    val assetId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    var portfolioEntity: PortfolioEntity? = null,

    var priority: Int = 0
) : BaseEntity() {
    fun update(count: Double, purchasePrice: BigDecimal, currencyType: CurrencyType, order: Int): PortfolioAssetEntity {
        this.count = count
        this.purchasePrice = purchasePrice
        this.currencyType = currencyType
        this.priority = order
        return this
    }

    fun delete() {
        portfolioEntity?.portfolioAssetEntityList?.remove(this)
    }

    fun toPortfolioAsset(asset: Asset): PortfolioAsset = PortfolioAsset(
        id = this.id!!,
        count = this.count,
        purchasePrice = this.purchasePrice,
        currencyType = this.currencyType,
        asset = asset
    )

    fun toSimplePortfolioAsset() = SimplePortfolioAsset(
        portfolioAssetId = this.id!!,
        count = this.count,
        purchasePrice = this.purchasePrice,
        currencyType = this.currencyType,
        assetId = this.assetId,
        priority = this.priority
    )

    fun toPortfolioAssetResponse(asset: Asset) = SimplePortfolioAssetResponse(
        portfolioAssetId =  this.id!!,
        assetId = this.assetId,
        count = this.count,
        purchasePrice = this.purchasePrice,
        currencyType = this.currencyType,
        tickerCode = asset.tickerCode,
        stockCode = asset.stockCode,
        name = asset.name,
        order = this.priority
    )
}
