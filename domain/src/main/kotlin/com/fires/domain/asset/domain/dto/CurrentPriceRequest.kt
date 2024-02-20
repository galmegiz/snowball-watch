package com.fires.domain.asset.domain.dto

import com.fires.common.constant.CurrencyType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "자산 현재가 요청")
data class CurrentPriceRequest(
    @Schema(title = "자산 id")
    val assetId: Long,
    @Schema(title = "통화타입")
    val currencyType: CurrencyType
)