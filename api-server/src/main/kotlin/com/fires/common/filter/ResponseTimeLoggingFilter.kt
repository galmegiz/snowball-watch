package com.fires.common.filter

import com.fires.common.logging.Log
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(1)
class ResponseTimeLoggingFilter : Filter, Log {
    companion object {
        private const val LONG_RESPONSE_TIME = 1000L
    }
    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain
    ) {
        val startTime = System.currentTimeMillis()
        val req: HttpServletRequest = request as HttpServletRequest
        chain.doFilter(request, response)
        val executionTime = System.currentTimeMillis() - startTime
        if (executionTime > LONG_RESPONSE_TIME) {
            log.warn(
                "Slow Api call request : {}, executionTime : {}",
                req.requestURI,
                executionTime
            )
        }
    }
}
