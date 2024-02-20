package com.fires.domain.exchangerate.adapter.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class EcosKeyStatisticRowDto(
    @JsonProperty(value = "CLASS_NAME")
    val className: String,
    @JsonProperty(value = "KEYSTAT_NAME")
    val keyStatName: String,
    @JsonProperty(value = "DATA_VALUE")
    val value: String,
    @JsonProperty(value = "CYCLE")
    val date: String
)
