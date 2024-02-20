package com.fires.domain.asset.application.port.out

import com.fires.domain.asset.constant.AssetMarketType
import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.asset.domain.entity.SimpleAssetDividend
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import java.time.LocalDate

interface AssetPort {
    fun save(command: AssetCreateCommand): Asset?
    fun saveAssetDividends(assetId: Long, commandList: List<AssetDividendAddCommand>): List<SimpleAssetDividend>
    fun saveAll(commandList: List<AssetCreateCommand>): List<Asset>
    fun queryAsset(word: String, pageable: Pageable): Slice<Asset>
    fun findAssetList(assetIds: Collection<Long>): List<Asset>
    fun findAsset(assetId: Long): Asset
    fun getAssetDividendListByPeriod(assetIds: List<Long>, from: LocalDate, to: LocalDate): Map<Long, List<SimpleAssetDividend>>
    fun updateAssetMarketType(assetId: Long, marketType: AssetMarketType)
    fun getAssetDividendsThisMonth(assetId: Collection<Long>): Map<Long, List<SimpleAssetDividend>>
    fun findAssetByMarketType(marketType: AssetMarketType, size: Int): List<Asset>
}
