package com.fires.domain.asset.adapter

import com.fires.common.logging.Log
import com.fires.domain.asset.adapter.repository.PopularStockRepository
import com.fires.domain.asset.application.port.out.PopularStockPort
import com.fires.domain.asset.domain.dto.PopulorStock
import com.fires.domain.asset.domain.entity.Asset
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PopularStockAdapter(
    private val popularStockRepository: PopularStockRepository
) : PopularStockPort, Log {
    @Transactional
    override fun save(asset: Asset): Boolean {
        popularStockRepository.save(asset.toPopularStockEntity())
        return true
    }

    @Transactional
    override fun saveAll(assetList: List<Asset>): Boolean {
        popularStockRepository.saveAll(assetList.map { it.toPopularStockEntity() })
        return true
    }

    @Transactional(readOnly = true)
    override fun readPopularStocks(size: Int): List<PopulorStock> {
        val pageRequest = PageRequest.of(0, size)
        return popularStockRepository.findAll(pageRequest).mapNotNull { it.toPopulorStock() }
    }
}
