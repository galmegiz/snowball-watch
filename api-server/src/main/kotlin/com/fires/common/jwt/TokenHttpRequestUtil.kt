package com.fires.common.jwt

import com.fires.common.auth.helper.jwt.JwtUtils
import com.fires.common.constant.CacheConstant
import com.fires.common.exception.AuthenticationException
import com.fires.common.exception.ErrorCode
import jakarta.servlet.http.HttpServletRequest
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

@Component
class TokenHttpRequestUtil(
    val jwtUtils: JwtUtils,
    val cacheManager: CacheManager
) {
    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer "
    }

    /**
     * http header token 취득
     */
    private fun getHttpRequestToken(request: HttpServletRequest): String {
        val bearerToken = request.getHeader(AUTHORIZATION_HEADER)
        return when {
            StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX) -> bearerToken.substring(
                BEARER_PREFIX.length
            )
            else -> throw AuthenticationException(ErrorCode.BAD_CREDENTIALS_ERROR)
        }
    }

    /**
     * 토큰 검증 및 토큰 취득
     */
    fun getToken(request: HttpServletRequest): String {
        val token: String = getHttpRequestToken(request)

        if (!jwtUtils.verifyToken(token)) {
            throw AuthenticationException(ErrorCode.BAD_CREDENTIALS_ERROR)
        }
        return token
    }

    /**
     * 사용자 ID 취득
     */
    fun getId(token: String): Long = jwtUtils.getClaim(token, "userId")?.toLong()
        ?: throw AuthenticationException(ErrorCode.BAD_CREDENTIALS_ERROR)

    /**
     * 사용자 email 취득
     */
    fun getEmail(token: String): String = jwtUtils.getClaim(token, "email")
        ?: throw AuthenticationException(ErrorCode.BAD_CREDENTIALS_ERROR)

    fun isLogoutUserToken(token: String): Boolean {
        val logoutCache = cacheManager.getCache(CacheConstant.LOGOUT_USER_TOKEN)
        return logoutCache?.get(token) != null
    }
}
