package com.fires.common.auth.domain.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class Token(
    @Schema(title = "권한 부여 유형")
    val grantType: String,

    @Schema(title = "접근 토큰")
    val accessToken: String,

    @Schema(title = "갱신 토큰")
    val refreshToken: String? = null,

    @Schema(title = "접근 토큰 유효 날짜")
    val accessTokenExpiresIn: Date,

    @Schema(title = "리프레쉬 토큰 유효 날짜")
    val refreshTokenExpiresIn: Date
)
