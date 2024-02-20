package com.fires.common.auth.adapter.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * refresh 토큰 저장용
 */
@Entity
@Table(name = "user_refresh_tokens")
class UserRefreshTokenEntity(
    @Id
    @Column(name = "user_key")
    private val key: String,
    @Column(name = "refresh_token")
    var value: String
) {
    /**
     * 토큰 update
     */
    fun updateValue(token: String): UserRefreshTokenEntity {
        value = token
        return this
    }
}
