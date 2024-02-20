package com.fires.domain.asset.application.port.`in`

import com.fires.domain.asset.constant.AssetMarketType
import com.fires.domain.asset.domain.dto.CurrentPriceRequest
import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.asset.domain.entity.Price
import org.springframework.data.domain.Slice

interface AssetUseCase {
    fun findAsset(request: AssetQueryCommand, email: String): Slice<Asset>
    fun findAssetsInIds(assetIds: Collection<Long>): List<Asset>
    fun updateAssetMarketType(assetId: Long, marketType: AssetMarketType)
    fun getAssetsCurrentPrice(currentPriceRequests: List<CurrentPriceRequest>): List<Price>
    fun getCurrentPriceAndPriceChange(asset: Asset): Price
}
