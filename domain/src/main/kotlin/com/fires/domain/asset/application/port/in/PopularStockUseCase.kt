package com.fires.domain.asset.application.port.`in`

import com.fires.domain.asset.domain.dto.PopulorStock

interface PopularStockUseCase {
    fun readPopularStocks(size: Int): List<PopulorStock>
}
