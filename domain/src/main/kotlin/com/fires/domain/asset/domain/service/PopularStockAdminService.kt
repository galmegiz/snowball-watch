package com.fires.domain.asset.domain.service

import com.fires.common.logging.Log
import com.fires.domain.asset.application.port.`in`.PopularStockAdminUseCase
import com.fires.domain.asset.application.port.out.AssetPort
import com.fires.domain.asset.application.port.out.PopularStockPort
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class PopularStockAdminService(
    private val assetPort: AssetPort,
    private val popularStockPort: PopularStockPort
) : Log, PopularStockAdminUseCase {
    override fun registPopularStock(name: String): Boolean {
        val asset = assetPort.queryAsset(name, PageRequest.of(0, 1))
        return popularStockPort.save(asset.first())
    }
}
