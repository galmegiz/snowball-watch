package com.fires.domain.user.application.port.out

import com.fires.domain.user.adapter.entity.UserEntity
import com.fires.domain.user.constant.OAuthChannelType
import com.fires.domain.user.domain.dto.User

interface UserPort {
    fun save(command: UserCreateCommand): User?
    fun findEntityById(id: Long): UserEntity?
    fun findValidUserByEmail(email: String): User?
    fun findValidUserByEmailAndOauthType(email: String, oAuthChannelType: OAuthChannelType): User?
    // fun findUserByEmail(email: String): User?
    fun updateLastLoginTime(user: User): Unit
    fun deleteUserByEmail(email: String): Boolean
    fun findValidUserById(userId: Long): User?
}
