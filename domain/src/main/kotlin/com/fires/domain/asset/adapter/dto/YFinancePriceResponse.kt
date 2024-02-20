package com.fires.domain.asset.adapter.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fires.common.constant.CurrencyType
import com.fires.domain.asset.domain.entity.Price
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

data class YFinancePriceResponse(
    val data: List<ResponseDetail>?,
    val message: String?,
    val status: String?
) {
    data class ResponseDetail(
        @JsonProperty(value = "Close")
        val close: String?,
        @JsonProperty(value = "Date")
        val date: String?
    )

    fun toPrice(assetId: Long, currencyType: CurrencyType): Price {
        val responseDetail = data?.firstOrNull()
        return Price(
            assetId,
            BigDecimal(responseDetail?.close ?: "0.0"),
            currencyType,
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(responseDetail?.date?.toLong() ?: Instant.now().toEpochMilli()),
                TimeZone.getDefault().toZoneId()),
            "",
            BigDecimal.ZERO,
            BigDecimal.ZERO
        )
    }

    fun toPrices(assetId: Long, currencyType: CurrencyType): List<Price> {
        return data?.map { responseDetail ->
            Price(
                assetId,
                BigDecimal(responseDetail.close ?: "0.0"),
                currencyType,
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(responseDetail.date?.toLong() ?: Instant.now().toEpochMilli()),
                    TimeZone.getDefault().toZoneId()),
                "",
                BigDecimal.ZERO,
                BigDecimal.ZERO,
            )
        } ?: emptyList()
    }
}
