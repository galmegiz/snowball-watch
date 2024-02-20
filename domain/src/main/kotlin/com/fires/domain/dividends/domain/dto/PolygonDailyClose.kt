package com.fires.domain.dividends.domain.dto

import com.fires.common.constant.CurrencyType
import com.fires.common.logging.Log
import com.fires.domain.asset.domain.dto.DailyClosePrice
import java.math.BigDecimal
import java.time.LocalDateTime

data class PolygonDailyClose(
    val close: String,
    val from: String,
    val symbol: String
) : Log {
    fun toDailyPrice(assetId: Long): DailyClosePrice {
        log.info("[Application Debug Log] OverseasDailyPriceResponse to DailyPrice {} ", this)
        return DailyClosePrice(
            assetId = assetId,
            closePrice = BigDecimal(close.takeIf { !it.isNullOrBlank() } ?: "0.0"),
            currencyType = CurrencyType.USD,
            accessTime = LocalDateTime.now(),
            sign = null
        )
    }
}
