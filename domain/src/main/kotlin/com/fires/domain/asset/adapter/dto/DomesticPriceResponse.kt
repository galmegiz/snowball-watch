package com.fires.domain.asset.adapter.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fires.common.constant.CurrencyType
import com.fires.common.logging.Log
import com.fires.domain.asset.domain.entity.Price
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * @Link <a href = "https://apiportal.koreainvestment.com/apiservice/apiservice-domestic-stock-quotations#L_07802512-4f49-4486-91b4-1050b6f5dc9d">한국 투자증권 국내주식시세</a>
 */
data class DomesticPriceResponse(
    @JsonProperty(value = "rt_cd") // 성공 실패 여부 0이외의 값 실패
    val returnCode: String = "",
    @JsonProperty(value = "msg_cd") // 응답코드
    val messageCode: String = "",
    @JsonProperty(value = "msg1") // 응답메시지
    val message: String = "",
    @JsonProperty(value = "output", required = false) // 응답상세
    val output: ResponseDetail? = null
) : Log {
    data class ResponseDetail(
        @JsonProperty(value = "stck_prpr") // 주식현재가
        val price: String?,
        @JsonProperty(value = "prdy_vrss") // 전일 대비
        val priceChange: String?,
        @JsonProperty(value = "prdy_vrss_sign") // 전일 대비 부호 1: 상한 2: 상승 3: 보합 4: 하한 5: 하락
        val priceChangeCode: String?,
        @JsonProperty(value = "prdy_ctrt") // 전일 대비 부호 1: 상한 2: 상승 3: 보합 4: 하한 5: 하락
        val priceChangeRatio: String?
    )
    fun toPrice(assetId: Long): Price {
        log.debug("[Application Debug Log] DomesticPriceResponse to Price {} ", this)
        // 한국은행 API 응닶값으로 ResponseDetail각 필드가 null 또는 빈값으로 넘어오는 경우 간헐적 발생, 단시간 내 API 다수 호출 시 발생하는 무제로 추정
        return Price(
            assetId,
            BigDecimal(this.output?.price?.takeIf { it.isNotBlank() } ?: "0.0"),
            CurrencyType.KRW,
            LocalDateTime.now(),
            output?.priceChangeCode,
            BigDecimal(this.output?.priceChange?.takeIf { it.isNotBlank() } ?: "0.0"),
            BigDecimal(this.output?.priceChangeRatio?.trim()?.takeIf { it.isNotBlank() } ?: "0.0")
        )
    }
}
