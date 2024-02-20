package com.fires.common.interceptor

import com.auth0.jwt.exceptions.TokenExpiredException
import com.fires.common.exception.AuthenticationException
import com.fires.common.exception.ErrorCode
import com.fires.common.jwt.TokenHttpRequestUtil
import com.fires.common.logging.Log
import com.fires.domain.user.domain.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

/**
 *  토큰 검증용 인터셉터
 */
@Component
class TokenVerifyInterceptor(
    private val userService: UserService,
    private val tokenHttpRequestUtil: TokenHttpRequestUtil
) : HandlerInterceptor, Log {
    companion object {
        const val profileKey = "userProfile"
    }

    /**
     * 토큰 검증
     */
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val method = request.method
        if (method.equals(HttpMethod.OPTIONS.name())) {
            return true
        }

        try {
            val token: String = tokenHttpRequestUtil.getToken(request)
            logoutUserTokenCheck(token)
            val userId = tokenHttpRequestUtil.getId(token)
            val email = tokenHttpRequestUtil.getEmail(token)
            val user = userService.findUserById(userId)
                ?: throw AuthenticationException(ErrorCode.INVALID_TOKEN_ERROR)

            request.setAttribute(profileKey, user)
            log.info("request userId: {}. email: {}. token: {}", userId, email, token)
        } catch (e: TokenExpiredException) {
            log.warn("access token is expired. msg {}.", e.message)
            throw AuthenticationException(ErrorCode.ACCESS_TOKEN_EXPIRED)
        } catch (e: Exception) {
            log.error(
                "error occurs on verifying access token. request uri - {}, authorization header value - {} msg - {}",
                request.requestURI,
                request.getHeader("Authorization"),
                e.message
            )
            throw AuthenticationException(ErrorCode.INVALID_TOKEN_ERROR, e)
        }
        return true
    }

    private fun logoutUserTokenCheck(token: String) {
        if (tokenHttpRequestUtil.isLogoutUserToken(token)) {
            log.warn("access token of logout user. msg {}.", token)
            throw AuthenticationException(ErrorCode.LOGOUT_USER_TOKEN)
        }
    }
}
