package com.fires.domain.exchangerate.adapter.client

import com.fires.common.constant.CacheConstant
import com.fires.common.logging.Log
import com.fires.domain.exchangerate.adapter.dto.EcosKeyStatisticResponse
import com.fires.domain.exchangerate.adapter.dto.EcosKeyStatisticRowDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

/**
 * 한국은행 통계 api
 * 달러뿐만 아니라 엔화, 유로화등 여러 국가들 환율정보 제공함. 지금은 달려 환율만 활용
 * 제약조건 : 날짜별로 조회불가
 * https://ecos.bok.or.kr/api/#/DevGuide/DevSpeciflcation/OA-1010
 */
@Component
class EcosClient(
    private val webClient: WebClient,
    @Value("\${external.ecos.base-url}")
    private val baseUrl: String,
    @Value("\${external.ecos.app-key}")
    private val appKey: String
) : Log {
    @Cacheable(cacheNames = [CacheConstant.ECOS])
    fun getUsdExchangeRate(): EcosKeyStatisticRowDto {
        val result = webClient.get()
            .uri("$baseUrl$appKey/json/kr/1/10")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono<EcosKeyStatisticResponse>()
            .block()?.keyStatisticList?.row ?: emptyList()
        return result.first { "환율" == it.className && "원/달러 환율(종가)" == it.keyStatName }
    }
}
