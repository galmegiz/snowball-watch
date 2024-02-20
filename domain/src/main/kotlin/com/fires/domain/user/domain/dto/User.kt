package com.fires.domain.user.domain.dto

import com.fires.domain.user.constant.UserType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import java.time.LocalDateTime

/**
 * 회원
 */
@Schema(name = "회원")
data class User(
    @Schema(title = "식별자")
    val id: Long,

    @Schema(title = "회원명")
    val userName: String? = null,

    @Schema(title = "비밀번호")
    val password: String,

    @Schema(title = "이메일")
    @field:Email
    val email: String,

    @Schema(title = "회원 타입")
    val userType: UserType,

    @Schema(title = "최근 접속일")
    val lastLoginAt: LocalDateTime,

    @Schema(title = "삭제 여부")
    val deletedAt: LocalDateTime?
) {

    fun isWithdrawal(): Boolean {
        return deletedAt != null
    }
}
