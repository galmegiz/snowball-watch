package com.fires.domain.asset.adapter

import com.fires.common.exception.ErrorCode
import com.fires.common.exception.ServiceException
import com.fires.common.logging.Log
import com.fires.domain.asset.adapter.repository.AssetRepository
import com.fires.domain.asset.application.port.out.AssetCreateCommand
import com.fires.domain.asset.application.port.out.AssetDividendAddCommand
import com.fires.domain.asset.application.port.out.AssetPort
import com.fires.domain.asset.constant.AssetMarketType
import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.asset.domain.entity.SimpleAssetDividend
import com.fires.domain.dividends.adapter.repository.AssetDividendRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.CollectionUtils
import java.time.LocalDate
import java.util.*
import kotlin.random.Random

@Component
@Transactional
class AssetAdapter(
    private val assetRepository: AssetRepository,
    private val assetDividendRepository: AssetDividendRepository
) : AssetPort, Log {
    @Transactional
    override fun save(command: AssetCreateCommand): Asset? {
        return assetRepository.save(command.toAssetEntity()).toAsset()
    }

    override fun saveAssetDividends(assetId: Long, commandList: List<AssetDividendAddCommand>): List<SimpleAssetDividend> {
        val assetEntity = assetRepository.findByIdOrNull(assetId) ?: return emptyList()
        assetEntity.addPortfolioAssetEntityList(commandList.map { it.toAssetDividendEntity() })
        TODO("Not yet implemented")
    }

    @Transactional
    override fun saveAll(commandList: List<AssetCreateCommand>): List<Asset> {
        return assetRepository.saveAll(commandList.map { it.toAssetEntity() }).map { it.toAsset() }
    }

    @Transactional(readOnly = true)
    override fun queryAsset(word: String, pageable: Pageable): Slice<Asset> =
        assetRepository.findByTickerCodeContainsIgnoreCaseOrStockCodeContainsIgnoreCaseOrNameContainingIgnoreCaseOrderByTickerCodeAsc(
            tickerCode = word.uppercase(),
            stockCode = word.uppercase(),
            name = word.uppercase(),
            pageable = pageable
        ).map { it.toAsset() }

    @Transactional(readOnly = true)
    override fun findAssetList(assetIds: Collection<Long>): List<Asset> = assetRepository.findByIdIn(assetIds).map { it.toAsset() }

    override fun findAsset(assetId: Long): Asset = assetRepository.findByIdOrNull(assetId)?.toAsset() ?: throw ServiceException(ErrorCode.ASSET_NOT_FOUND)

    override fun getAssetDividendListByPeriod(assetIds: List<Long>, from: LocalDate, to: LocalDate): Map<Long, List<SimpleAssetDividend>> {
        return assetRepository.findByAssetByDividendPeriod(assetIds, from, to)
            .associate { it.id!! to it.assetDividendEntities.map { dividend -> dividend.toSimpleAssetDividend() } }
    }
    override fun updateAssetMarketType(assetId: Long, marketType: AssetMarketType) {
        val assetEntity = assetRepository.findByIdOrNull(assetId) ?: throw ServiceException(ErrorCode.ASSET_NOT_FOUND)
        assetEntity.marketType = marketType
    }

    override fun getAssetDividendsThisMonth(assetIds: Collection<Long>): Map<Long, List<SimpleAssetDividend>> {
        val thisMonth = LocalDate.now()
        val startOfMonth = thisMonth.withDayOfMonth(1)
        val endOfMonth = thisMonth.withDayOfMonth(thisMonth.lengthOfMonth())
        val assetDividendMap = assetDividendRepository.findByIdInAndPayDateBetween(assetIds, startOfMonth, endOfMonth)
            .groupBy(keySelector = { it.assetEntity!!.id!! }, valueTransform = { it.toSimpleAssetDividend() })

        if (CollectionUtils.isEmpty(assetDividendMap)) {
            return Collections.emptyMap()
        }

        return assetDividendMap
    }

    override fun findAssetByMarketType(marketType: AssetMarketType, size: Int): List<Asset> {
        val rand = Random.nextInt(2)
        val pageable = PageRequest.of(rand, size)
        return assetRepository.findByMarketType(marketType, pageable).map { it.toAsset() }
    }
}
