package com.fires.domain.dividends.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.dividends.domain.entity.Dividend
import java.time.Instant
import java.time.LocalDate
import java.util.*

data class YFinanceDividendResponse(
    val data: List<ResponseDetail>?,
    val message: String?,
    val status: String?
) {
    data class ResponseDetail(
        @JsonProperty(value = "Date")
        val date: String?,
        @JsonProperty(value = "Dividends")
        val dividends: String?
    )

    fun toDividend(asset: Asset): List<Dividend> {
        return data?.map { responseDetail ->
            val date = LocalDate.ofInstant(
                Instant.ofEpochMilli(responseDetail.date?.toLong() ?: Instant.now().toEpochMilli()),
                TimeZone.getDefault().toZoneId()
            )

            Dividend(
                assetId = asset.assetId,
                ticker = asset.tickerCode,
                stockCode = asset.stockCode,
                cashAmount = responseDetail.dividends?.toDouble() ?: 0.0,
                exDividendDate = date,
                payDate = date
            )
        } ?: emptyList()
    }
}
