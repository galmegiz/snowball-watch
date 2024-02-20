package com.fires.domain.user.application.port.`in`

import com.fires.domain.user.constant.OAuthChannelType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email

/**
 * 로그인 request
 */
@Schema(title = "로그인 요청")
data class UserLoginCommand(
    @Schema(title = "이메일")
    @field:Email
    val email: String,
    // @field:Password
    @Schema(title = "비밀번호")
    val password: String,
    @Schema(title = "가입채널")
    val oAuthChannelType: OAuthChannelType
)
