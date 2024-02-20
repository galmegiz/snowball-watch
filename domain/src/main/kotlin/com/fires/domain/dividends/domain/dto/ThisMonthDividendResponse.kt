package com.fires.domain.dividends.domain.dto

import com.fires.common.constant.CurrencyType
import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.dividends.domain.entity.Dividend
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

@Schema(title = "월 배당 정보")
data class ThisMonthDividendResponse(
    @Schema(name = "티커")
    val name: String,
    @Schema(name = "티커")
    val tickerCode: String? = "",
    @Schema(name = "주식코드")
    val stockCode: String? = "",
    @Schema(name = "예상배당금지급일")
    val expectedPayDate: LocalDate?,
    @Schema(name = "배당락일")
    val exDividendDate: LocalDate?,
    @Schema(name = "예상배당금")
    var expectedDividends: BigDecimal,
    @Schema(name = "통화")
    val currencyType: CurrencyType = CurrencyType.KRW
){

    constructor(
        asset: Asset,
        dividend: Dividend,
        expectedDividends: BigDecimal
    ) : this(name = asset.name,
        tickerCode = asset.tickerCode,
        stockCode = asset.stockCode,
        expectedPayDate = dividend.payDate,
        exDividendDate = dividend.exDividendDate,
        expectedDividends = expectedDividends
    )

    init{
        this.expectedDividends = this.expectedDividends.setScale(2, RoundingMode.HALF_UP)
    }
}
