package com.fires.domain.asset.application.port.out

import com.fires.domain.asset.constant.FrequencyType
import com.fires.domain.dividends.adapter.entity.AssetDividendEntity
import java.math.BigDecimal
import java.time.LocalDate

data class AssetDividendAddCommand(
    val frequencyType: FrequencyType,
    val dividend: BigDecimal,
    val exDividendDate: LocalDate?,
    val payDate: LocalDate?
) {
    fun toAssetDividendEntity() = AssetDividendEntity(
        frequencyType = this.frequencyType,
        dividend = this.dividend,
        exDividendDate = this.exDividendDate,
        payDate = this.payDate
    )
}
