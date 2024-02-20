package com.fires.domain.user.application.port.`in`

import com.fires.domain.user.constant.OAuthChannelType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email

/**
 * 회원 가입 요청
 */
@Schema(name = "회원 가입")
data class UserSignupCommand(
    @Schema(title = "회원 이름")
    val userName: String,

    @Schema(title = "OAuth 채널")
    val oAuthChannelType: OAuthChannelType,

    // @field:Password
    @Schema(title = "비밀번호")
    val password: String,

    @Schema(title = "이메일")
    @field:Email
    val email: String
)
