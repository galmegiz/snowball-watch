package com.fires.domain.user.application.port.`in`

import com.fires.domain.user.constant.OAuthChannelType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email

data class UserExistCheckCommand(
    @Schema(title = "이메일")
    @field:Email
    val email: String,
    val oAuthChannelType: OAuthChannelType
)
