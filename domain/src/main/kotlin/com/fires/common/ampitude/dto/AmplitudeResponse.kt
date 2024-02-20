package com.fires.common.ampitude.dto

import com.fasterxml.jackson.annotation.JsonProperty

class AmplitudeResponse(
    val code: Int,
    @JsonProperty(value = "events_ingested")
    val eventsIngested: Int,
    @JsonProperty(value = "payload_size_bytes")
    val payLoadSizeBytes: Int,
    @JsonProperty(value = "server_upload_time")
    val serverUploadTime: Long
) {
}