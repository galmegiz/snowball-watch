package com.fires.common.interceptor

import com.fires.common.exception.ErrorCode
import com.fires.common.exception.ServiceException
import com.fires.common.logging.Log
import com.google.common.net.HttpHeaders
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AccessTokenVerifyInterceptor(
    @Value("\${access.token}")
    val accessToken: String
) : HandlerInterceptor, Log {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (request.method == HttpMethod.OPTIONS.name()) {
            return true
        }
        val token: String = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (accessToken != token) {
            log.warn("Invalid Login request With AccessToken : {}", token)
            throw ServiceException(ErrorCode.UNAUTHORIZED_LOGIN_REQUEST)
        }
        return true
    }
}
