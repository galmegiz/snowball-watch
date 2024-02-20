package com.fires.domain.dividends.domain.dto

import com.fires.domain.dividends.domain.AnnualDividendCalculator
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Month

@Schema(title = "메인페이지 상단 1년 배당정보")
data class AnnualDividendResponse(
    @Schema(title = "이번달 배당금")
    var thisMonthDividend: BigDecimal,
    @Schema(title = "지난 배당 대비")
    var dividendChange: BigDecimal,
    @Schema(title = "연간 총 배당금")
    var annualDividend: BigDecimal,
    @Schema(title = "투자 배당률, 현재는 튜자배당률의 평균")
    var dividendPriceRatio: BigDecimal,
    @Schema(title = "시가 배당률(배당수익률), 현재는 시가배당률의 평균")
    var dividendYieldRatio: BigDecimal,
    @Schema(title = "납부할 세금")
    var unPaidTax: BigDecimal,
    @Schema(title = "납부한 세금")
    var paidTax: BigDecimal,
    @Schema(title = "월별 배당금")
    val monthlyDividends: MutableMap<Month, BigDecimal>
){
    constructor(
        annualAssetCalculator: AnnualDividendCalculator,
        dividendTaxCalculator: DividendTaxCalculator
    ) : this(
        thisMonthDividend = annualAssetCalculator.thisMonthDividend,
        dividendChange = annualAssetCalculator.getDividendChange(),
        annualDividend = annualAssetCalculator.totalDividend,
        dividendPriceRatio = annualAssetCalculator.getAverageDividendPriceRatio(),
        dividendYieldRatio = annualAssetCalculator.getAverageDividendYieldRatio(),
        unPaidTax = dividendTaxCalculator.unPaidTax,
        paidTax = dividendTaxCalculator.paidTax,
        monthlyDividends = annualAssetCalculator.monthlyDividend
    )

    init{
        this.thisMonthDividend = this.thisMonthDividend.setScale(2, RoundingMode.HALF_UP)
        this.dividendChange = this.dividendChange.setScale(2, RoundingMode.HALF_UP)
        this.annualDividend = this.annualDividend.setScale(2, RoundingMode.HALF_UP)
        this.dividendPriceRatio = this.dividendPriceRatio.setScale(2, RoundingMode.HALF_UP)
        this.dividendYieldRatio = this.dividendYieldRatio.setScale(2, RoundingMode.HALF_UP)
        this.unPaidTax = this.unPaidTax.setScale(2, RoundingMode.HALF_UP)
        this.paidTax = this.paidTax.setScale(2, RoundingMode.HALF_UP)
        this.monthlyDividends.entries.forEach { (key, value) -> this.monthlyDividends[key] = value.setScale(2, RoundingMode.HALF_UP) }
    }

}
