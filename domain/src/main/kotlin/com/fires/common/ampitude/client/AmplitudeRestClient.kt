package com.fires.common.ampitude.client

import com.fires.common.ampitude.constant.UserEventType
import com.fires.common.ampitude.dto.AmplitudeRequest
import com.fires.common.logging.Log
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

class AmplitudeRestClient(
    private val restTemplate: RestTemplate,
    @Value("\${amplitude.api-key}")
    private val apiKey: String
) : Log {
    companion object {
        private const val API_URL = "https://api2.amplitude.com/2/httpapi"
    }

    private fun setRequest(amplitudeRequest: AmplitudeRequest): RequestEntity<AmplitudeRequest> {
        val uri = UriComponentsBuilder.fromHttpUrl(API_URL)
            .build()
            .encode()
            .toUri()

        return RequestEntity.post(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.ALL)
            .body(amplitudeRequest)
    }


    fun sendSignInEvent(email: String, eventType: UserEventType): String? {
        val event = AmplitudeRequest.Event(email, eventType)
        val request = AmplitudeRequest(apiKey, listOf(event))
        val requestEntity = setRequest(request)
        try {
            val result = restTemplate.exchange(requestEntity, String.javaClass)

            log.info(" amp result : {} ", result)
            return result.body.toString()
        } catch (e: Exception) {
            log.error("error !", e)
        }
        return null
    }
}