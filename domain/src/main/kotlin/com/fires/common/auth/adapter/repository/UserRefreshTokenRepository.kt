package com.fires.common.auth.adapter.repository

import com.fires.common.auth.adapter.entity.UserRefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * refresh 토큰 관련
 */
interface UserRefreshTokenRepository : JpaRepository<UserRefreshTokenEntity, Long> {
    fun findByKey(key: String?): UserRefreshTokenEntity?
}
