package com.fires.domain.asset.adapter.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class TokenRequest(
    @JsonProperty(value = "appkey")
    val appKey: String,
    @JsonProperty(value = "appsecret")
    val appSecret: String
) {
    @JsonProperty(value = "grant_type")
    val grantType = "client_credentials"
}
