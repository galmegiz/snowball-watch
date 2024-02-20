package com.fires.domain.dividends.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fires.common.constant.CurrencyType
import com.fires.common.logging.Log
import com.fires.domain.asset.domain.entity.Price
import java.math.BigDecimal
import java.time.LocalDateTime

data class PolygonPrevClose(
    @JsonProperty("c") // 종가
    val closePrice: String
) : Log {
    fun toPrice(assetId: Long): Price {
        log.debug("[Application Debug Log] OverseasPriceResponse to Price {} ", this)
        return Price(
            assetId,
            BigDecimal(this.closePrice.takeIf { !it.isNullOrBlank() } ?: "0.0"),
            CurrencyType.USD,
            LocalDateTime.now(),
            "0",
            BigDecimal.ZERO,
            BigDecimal.ZERO
        )
    }
}

