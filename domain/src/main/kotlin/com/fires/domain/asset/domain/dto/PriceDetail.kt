package com.fires.domain.asset.domain.dto

import com.fires.common.constant.CurrencyType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class PriceDetail(
    @Schema(title = "자산 아이디")
    val assetId: Long,

    @Schema(title = "자산 현재가")
    val currentPrice: String,

    @Schema(title = "통화")
    val currencyType: CurrencyType,

    @Schema(title = "현재가 검색 시간")
    val accessTime: LocalDateTime
)
