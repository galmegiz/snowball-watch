package com.fires.domain.asset.domain.entity

import com.fires.domain.asset.constant.FrequencyType
import com.fires.domain.dividends.domain.entity.Dividend
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

data class SimpleAssetDividend(
    val id: Long = 0,
    val assetId: Long,
    val frequencyType: FrequencyType,
    val dividend: BigDecimal,
    val exDividendDate: LocalDate?,
    val payDate: LocalDate?
){
    companion object{
        fun fromDividend(dividend: Dividend, usdExchangeRate: Double = 1.0): SimpleAssetDividend {
            return SimpleAssetDividend(
                assetId = dividend.assetId,
                frequencyType = FrequencyType.NONE,
                dividend = BigDecimal(dividend.cashAmount * usdExchangeRate),
                exDividendDate = dividend.exDividendDate,
                payDate = dividend.payDate
            )
        }
    }

    init{
        this.dividend.setScale(2, RoundingMode.HALF_UP)
    }
}
