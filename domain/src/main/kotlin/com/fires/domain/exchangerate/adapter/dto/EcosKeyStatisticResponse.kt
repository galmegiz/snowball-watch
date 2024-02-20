package com.fires.domain.exchangerate.adapter.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class EcosKeyStatisticResponse(
    @JsonProperty(value = "KeyStatisticList")
    val keyStatisticList: EcosKeyStatisticDto
)

data class EcosKeyStatisticDto(
    val row: List<EcosKeyStatisticRowDto>
)
