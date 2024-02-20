package com.fires.common.ampitude.client

import com.fires.common.ampitude.constant.UserEventType
import com.fires.common.ampitude.dto.AmplitudeErrorResponse
import com.fires.common.ampitude.dto.AmplitudeRequest
import com.fires.common.logging.Log
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Component
class AmplitudeClient(
    private val webClient: WebClient,
    @Value("\${amplitude.api-key}")
    private val apiKey: String
) : Log {
    companion object {
        private const val API_URL = "https://api2.amplitude.com/2/httpapi"
    }

    fun sendEvent(email: String, eventType: UserEventType): String? {
        val event = AmplitudeRequest.Event(email, eventType)
        val request = AmplitudeRequest(apiKey, listOf(event))
        return WebClient.
            builder().build().post()
            .uri(API_URL)
            .bodyValue(request)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(
                HttpStatus.BAD_REQUEST::equals
            ) { it ->
                val response = it.bodyToMono<AmplitudeErrorResponse>()
                throw RuntimeException(response.toString())
            }
            .bodyToMono<String>()
            .block()
    }
}

