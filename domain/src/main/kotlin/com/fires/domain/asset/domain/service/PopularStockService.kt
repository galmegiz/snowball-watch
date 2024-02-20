package com.fires.domain.asset.domain.service

import com.fires.common.logging.Log
import com.fires.domain.asset.application.port.`in`.PopularStockUseCase
import com.fires.domain.asset.application.port.out.PopularStockPort
import com.fires.domain.asset.constant.PopularStockConstant.MAX_QUERY_SIZE
import com.fires.domain.asset.domain.dto.PopulorStock
import org.springframework.stereotype.Service

@Service
class PopularStockService(
    private val popularStockPort: PopularStockPort
) : Log, PopularStockUseCase {
    override fun readPopularStocks(size: Int): List<PopulorStock> {
        val validSize = when {
            size > MAX_QUERY_SIZE -> MAX_QUERY_SIZE
            else -> size
        }
        return popularStockPort.readPopularStocks(validSize)
    }
}
