package com.fires.domain.asset.domain.dto

import com.fires.common.constant.CurrencyType
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDateTime

@Schema(name = "자산 현재가")
data class PriceChange(
    @Schema(title = "자산 아이디")
    val assetId: Long,

    @Schema(title = "통화")
    val currencyType: CurrencyType,

    @Schema(title = "현재가 검색 시간")
    val accessTime: LocalDateTime,

    @Schema(title = "대비기호(등락여부)")
    val sign: String?,

    @Schema(title = "전일 종가와 당일 현재가의 차이")
    val priceChange: BigDecimal,

    @Schema(title = "등락율")
    val priceChangeRate: BigDecimal
)
