package com.fires.domain.asset.domain.entity

import com.fires.domain.asset.adapter.entity.PopularStockEntity
import com.fires.domain.asset.constant.AssetCategoryType
import com.fires.domain.asset.constant.AssetMarketType
import com.fires.domain.asset.constant.CountryType
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 자산
 */
@Schema(name = "자산")
data class Asset(
    @Schema(title = "자산 아이디")
    val assetId: Long,

    @Schema(title = "티커코드")
    val tickerCode: String? = null,

    @Schema(title = "종목코드")
    val stockCode: String? = null,

    @Schema(title = "자산명")
    val name: String,

    @Schema(title = "국가")
    val countryType: CountryType,

    @Schema(title = "자산 시장 타입")
    var marketType: AssetMarketType,

    @Schema(title = "자산 카테고리 타입")
    val assetCategoryType: AssetCategoryType
) {
    fun toPopularStockEntity() = PopularStockEntity(
        tickerCode = this.tickerCode,
        stockCode = this.stockCode,
        name = this.name
    )

    fun assetWithMarket(assetMarketType: AssetMarketType): Asset {
        return Asset(
            assetId = this.assetId,
            tickerCode = this.tickerCode,
            stockCode = this.stockCode,
            name = this.name,
            countryType = this.countryType,
            marketType = assetMarketType,
            assetCategoryType = this.assetCategoryType
        )
    }

    fun updateMarket(assetMarketType: AssetMarketType) {
        this.marketType = assetMarketType
    }
}
