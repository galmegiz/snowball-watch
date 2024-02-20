package com.fires.domain.portfolio.application.port.`in`

import com.fires.domain.asset.constant.FrequencyType
import java.time.LocalDate

data class AssetDividendSaveRequest(
    val frequencyType: FrequencyType = FrequencyType.NONE,
    val dividend: Double = 0.0,
    val exDividendDate: LocalDate? = null,
    val payDate: LocalDate? = null,
    val assetId: Long
)
