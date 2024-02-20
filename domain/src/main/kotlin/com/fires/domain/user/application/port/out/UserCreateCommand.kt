package com.fires.domain.user.application.port.out

import com.fires.domain.user.constant.OAuthChannelType
import com.fires.domain.user.constant.UserType

/**
 * 회원 data port
 */
data class UserCreateCommand(
    val userName: String,
    val password: String,
    val email: String,
    val userType: UserType = UserType.ASSOCIATE,
    val oAuthChannelType: OAuthChannelType
)
