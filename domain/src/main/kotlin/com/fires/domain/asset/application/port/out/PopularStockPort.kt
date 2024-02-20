package com.fires.domain.asset.application.port.out

import com.fires.domain.asset.domain.dto.PopulorStock
import com.fires.domain.asset.domain.entity.Asset

interface PopularStockPort {
    fun save(asset: Asset): Boolean
    fun saveAll(assetList: List<Asset>): Boolean
    fun readPopularStocks(size: Int): List<PopulorStock>
}
