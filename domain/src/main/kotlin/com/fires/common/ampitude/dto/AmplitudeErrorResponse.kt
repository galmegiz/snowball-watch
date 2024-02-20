package com.fires.common.ampitude.dto

data class AmplitudeErrorResponse(val code: Int,
    val error: String,
    val missing_field: String
)
