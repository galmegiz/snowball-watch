package com.fires.domain.asset.adapter.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fires.common.constant.CurrencyType
import com.fires.common.logging.Log
import com.fires.domain.asset.domain.dto.DailyClosePrice
import java.math.BigDecimal
import java.time.LocalDateTime

data class OverseasDailyPriceResponse(
    @JsonProperty(value = "rt_cd") // 성공 실패 여부
    val returnCode: String = "",
    @JsonProperty(value = "msg_cd") // 응답 코드
    val messageCode: String = "",
    @JsonProperty(value = "msg1") // 응답 메시지
    val message: String = "",
    @JsonProperty(value = "output2", required = false) // 응답 상세
    val output2: List<OutputDetail>? = null
) : Log {
    data class OutputDetail(
/*        @JsonProperty(value = "xymd") // 일자
        @DateTimeFormat(pattern = "yyyyMMdd")
        val date: LocalDate?,*/
        @JsonProperty("clos") // 해당 일자의 종가
        val close: String?,
        @JsonProperty("sign") // 대비기호
        val sign: String?
    )

    fun toDailyPrice(assetId: Long): DailyClosePrice {
        log.debug("[Application Debug Log] OverseasDailyPriceResponse to DailyPrice {} ", this)
        // 한국은행 API 응닶값으로 ResponseDetail각 필드가 null 또는 빈값으로 넘어오는 경우 간헐적 발생, 단시간 내 API 다수 호출 시 발생하는 무제로 추정
        val outputDetail = this.output2?.getOrNull(0)
        return DailyClosePrice(
            assetId = assetId,
            closePrice = BigDecimal(outputDetail?.close?.takeIf { it.isNotBlank() } ?: "0.0"),
            currencyType = CurrencyType.USD,
            accessTime = LocalDateTime.now(),
            sign = outputDetail?.sign
        )
    }
}
