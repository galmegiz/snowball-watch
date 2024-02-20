package com.fires.domain.user.adapter.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fires.common.entity.BaseEntity
import com.fires.domain.user.constant.OAuthChannelType
import com.fires.domain.user.constant.UserType
import com.fires.domain.user.domain.dto.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    val id: Long? = null,

    /**
     * 패스워드
     */
    var password: String? = null,

    /**
     * 회원명
     */
    var userName: String? = null,

    /**
     * OAuth 채널
     */
    @Enumerated(EnumType.STRING)
    val oAuthChannelType: OAuthChannelType = OAuthChannelType.NONE,

    /**
     * 회원 타입(준회원, 정회원)
     */
    @Enumerated(EnumType.STRING)
    val userType: UserType = UserType.ASSOCIATE,

    /**
     * 최근 접속일
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    var lastLoginAt: LocalDateTime,

    /**
     * 이메일
     */
    val email: String,

    /**
     * 탈퇴 일자
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    var deletedAt: LocalDateTime? = null
) : BaseEntity() {
    /**
     * 회원 탈퇴
     * 소프트 삭제함
     */
    fun withdrawal() {
        deletedAt = LocalDateTime.now()
    }

    fun updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now()
    }

    fun toUser(): User = User(
        id = this.id ?: 0,
        userName = this.userName,
        password = this.password ?: "",
        email = this.email,
        userType = this.userType,
        lastLoginAt = this.lastLoginAt,
        deletedAt = this.deletedAt
    )
}
