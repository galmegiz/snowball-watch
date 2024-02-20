package com.fires.domain.exchangerate.domain.dto

import com.fires.common.constant.CurrencyType
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 환율 정보
 */
@Schema(name = "환율 정보")
data class ExchangeRate(
    @Schema(
        title = "종가",
        description = "매매기준율 종가"
    )
    val value: Double,

    @Schema(title = "통화타입")
    val currencyType: CurrencyType
)
