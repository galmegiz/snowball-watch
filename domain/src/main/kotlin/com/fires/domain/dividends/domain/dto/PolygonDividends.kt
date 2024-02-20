package com.fires.domain.dividends.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.dividends.domain.entity.Dividend
import java.time.LocalDate

/**
 * Polygon 배당금 정보
 */
data class PolygonDividends(
    val ticker: String?,
    @JsonProperty(value = "cash_amount")
    val cashAmount: Double?,
    @JsonProperty(value = "declaration_date")
    val declarationDate: String?, // 배당 발표 날짜
    @JsonProperty(value = "dividend_type")
    val dividendType: String?, // 배당타입
    @JsonProperty(value = "ex_dividend_date")
    val exDividendDate: String?, // 배당금 없이 주식이 처음 거래되는 날짜
    val frequency: Int?, // 배당주기
    @JsonProperty(value = "pay_date")
    val payDate: String? // 배당금 지급 날짜
){
    fun toDividend(asset: Asset): Dividend {
        return Dividend(
            assetId = asset.assetId,
            ticker = ticker,
            stockCode = null,
            cashAmount = cashAmount ?: 0.0,
            exDividendDate = LocalDate.parse(exDividendDate ?: LocalDate.now().toString()),
            payDate = LocalDate.parse(payDate ?: LocalDate.now().toString()),
        )
    }
}
