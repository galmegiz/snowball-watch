package com.fires.domain.portfolio.application.port.out

import com.fires.common.constant.CurrencyType
import java.math.BigDecimal

data class PortfolioAssetUpdateCommand(
    val portfolioAssetId: Long,
    val purchasePrice: BigDecimal,
    val count: Double,
    val currencyType: CurrencyType,
    val order: Int
)
