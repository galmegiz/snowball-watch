package com.fires.domain.portfolio.application.port.out

import com.fires.common.constant.CurrencyType
import com.fires.domain.portfolio.adapter.entity.PortfolioAssetEntity
import java.math.BigDecimal

data class PortfolioAssetAddCommand(
    val assetId: Long,
    val purchasePrice: BigDecimal,
    val count: Double,
    val currencyType: CurrencyType,
    val priority: Int
) {
    fun toPortfolioEntity() = PortfolioAssetEntity(
        count = this.count,
        assetId = this.assetId,
        purchasePrice = this.purchasePrice,
        currencyType = this.currencyType,
        priority = this.priority
    )
}
