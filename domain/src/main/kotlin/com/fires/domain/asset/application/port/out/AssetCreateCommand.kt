package com.fires.domain.asset.application.port.out

import com.fires.domain.asset.adapter.entity.AssetEntity

/**
 * 자산 data port
 */
data class AssetCreateCommand(
    val tickerCode: String? = null,
    val stockCode: String? = null,
    val name: String,
    val category: CategoryCreateCommand
) {
    fun toAssetEntity(): AssetEntity {
        return AssetEntity(
            tickerCode = this.tickerCode,
            stockCode = this.stockCode,
            name = this.name,
            countryType = this.category.countryType,
            marketType = this.category.marketType,
            assetCategoryType = this.category.assetCategoryType
        )
    }
}
