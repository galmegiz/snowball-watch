package com.fires.domain.asset.domain.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 인기주식
 */
@Schema(name = "인기주식")
data class PopulorStock(
    @Schema(title = "티커코드")
    val tickerCode: String? = null,

    @Schema(title = "종목코드")
    val stockCode: String? = null,

    @Schema(title = "자산명")
    val name: String
)
