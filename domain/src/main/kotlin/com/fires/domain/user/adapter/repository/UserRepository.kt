package com.fires.domain.user.adapter.repository

import com.fires.domain.user.adapter.entity.UserEntity
import com.fires.domain.user.constant.OAuthChannelType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findUserEntityByEmail(email: String): UserEntity?

    @Query("SELECT u FROM UserEntity u WHERE u.email = :email AND u.oAuthChannelType = :oAuthChannelType")
    fun findUserEntityByEmailAndOAuthChannelType(email: String, oAuthChannelType: OAuthChannelType): UserEntity?
    fun findUserEntityById(userId: Long): UserEntity?
}
