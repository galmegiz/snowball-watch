package com.fires.domain.asset.adapter.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fires.common.constant.CurrencyType
import com.fires.common.logging.Log
import com.fires.domain.asset.domain.entity.Price
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * @Link <a href="https://apiportal.koreainvestment.com/apiservice/apiservice-domestic-stock-current#L_3eeac674-072d-4674-a5a7-f0ed01194a81">해외주식현재가 </a>
 */
data class OverseasPriceResponse(
    @JsonProperty(value = "rt_cd") // 성공 실패 여부
    val returnCode: String = "",
    @JsonProperty(value = "msg_cd") // 응답 코드
    val messageCode: String = "",
    @JsonProperty(value = "msg1") // 응답 메시지
    val message: String = "",
    @JsonProperty(value = "output", required = false) // 응답 상세
    val output: ResponseDetail? = null
) : Log {
    data class ResponseDetail(
        @JsonProperty(value = "rsym") // 실시간 조회종목 코드
        val symbol: String?,
        @JsonProperty("last") // 현재가
        val price: String?,
        @JsonProperty("sign") // 대비기호
        val sign: String?,
        @JsonProperty(value = "diff") // 전일 종가와 당일 현재가의 차이
        val priceChange: String?,
        @JsonProperty("rate") // 등락율
        val priceChangeRate: String?
    )

    fun toPrice(assetId: Long): Price {
        log.debug("[Application Debug Log] OverseasPriceResponse to Price {} ", this)
        // 한국은행 API 응닶값으로 ResponseDetail각 필드가 null 또는 빈값으로 넘어오는 경우 간헐적 발생, 단시간 내 API 다수 호출 시 발생하는 무제로 추정
        return Price(
            assetId,
            BigDecimal(this.output?.price?.takeIf { it.isNotBlank() } ?: "0.0"),
            CurrencyType.USD,
            LocalDateTime.now(),
            output?.sign,
            BigDecimal(this.output?.priceChange?.takeIf { it.isNotBlank() } ?: "0.0"),
            BigDecimal(this.output?.priceChangeRate?.trim()?.takeIf { it.isNotBlank() } ?: "0.0")
        )
    }
}
