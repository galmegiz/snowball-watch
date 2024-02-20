package com.fires.common.ampitude.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fires.common.ampitude.constant.UserEventType

data class AmplitudeRequest(
    @JsonProperty(value = "api_key")
    val apiKey: String,
    val events: List<Event>
){
    data class Event(
        @JsonProperty(value = "user_id")
        val userId: String,
        @JsonProperty(value = "event_type")
        val eventType: UserEventType
    )
}

