package com.fires.domain.asset.domain.dto

import com.fires.common.constant.CurrencyType
import java.math.BigDecimal
import java.time.LocalDateTime

data class DailyClosePrice(
    val assetId: Long,
    val closePrice: BigDecimal,
    val currencyType: CurrencyType,
    val accessTime: LocalDateTime,
    var sign: String?
)
