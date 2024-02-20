package com.fires.domain.asset.application.port.out

import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.asset.domain.entity.Price
import java.time.LocalDate

interface AssetPricePort {
    fun getAssetPriceOnDate(asset: Asset, localDate: LocalDate): List<Price>
    fun getAssetsCurrentPrice(assets: List<Asset>): List<Price>
    fun getAssetCurrentPrice(asset: Asset): Price
}
