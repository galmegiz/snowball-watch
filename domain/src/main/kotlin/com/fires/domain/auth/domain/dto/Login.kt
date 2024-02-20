package com.fires.domain.auth.domain.dto

import com.fires.common.auth.domain.dto.Token
import com.fires.domain.user.constant.UserType
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 로그인 response
 */
data class Login(

    @Schema(title = "사용자 타입(준회원, 정회원")
    val userType: UserType,

    @Schema(title = "인증 토큰")
    val token: Token
)
