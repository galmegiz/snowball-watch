package com.fires.domain.dividends.adapter.client

import com.fires.common.exception.ErrorCode
import com.fires.common.exception.ServiceException
import com.fires.common.logging.Log
import com.fires.domain.dividends.constant.PolygonConstant
import com.fires.domain.dividends.domain.dto.PolygonDailyClose
import com.fires.domain.dividends.domain.dto.PolygonDividends
import com.fires.domain.dividends.domain.dto.PolygonDividendsResponse
import com.fires.domain.dividends.domain.dto.PolygonPrevClose
import com.fires.domain.dividends.domain.dto.PolygonPrevCloseResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.time.LocalDate
import java.time.YearMonth

@Component
class PolygonClient(
    @Value("\${polygon.api-key}")
    val apiKey: String,
    private val webClient: WebClient
) : Log {
    fun readDividends(tickerCode: String, from: YearMonth): List<PolygonDividends> {
        var fromString = from.atDay(1)
        var endString = from.plusYears(1).atDay(1)
        val url = String.format(PolygonConstant.POLYGON_DIVIDENDS_QUERY, tickerCode, apiKey, fromString, endString)
        try {
            return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono<PolygonDividendsResponse>()
                .block()?.results ?: emptyList()
        } catch (e: Exception) {
            log.error("readDividends fail!! tickerCode = {}, error = {}, from = {}", tickerCode, fromString, e.message)
        }
        return emptyList()
    }

    // todo: 중복되는 부분 리팩토링
    fun readThisMonthDividends(tickerCode: String, from: YearMonth): List<PolygonDividends> {
        val fromString = from.atDay(1)
        val endString = from.atEndOfMonth()
        val url = String.format(PolygonConstant.POLYGON_DIVIDENDS_QUERY_THIS_MONTH, tickerCode, apiKey, fromString, endString)
        try {
            return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono<PolygonDividendsResponse>()
                .block()?.results ?: emptyList()
        } catch (e: Exception) {
            log.error("readDividends fail!! tickerCode = {}, error = {}, from = {}", tickerCode, fromString, e.message)
        }
        return emptyList()
    }

    fun readPreviousClose(tickerCode: String): PolygonPrevClose {
        val url = String.format(PolygonConstant.POLYGON_PREVIOUS_CLOSE_QUERY, tickerCode, apiKey)
        try {
            return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono<PolygonPrevCloseResponse>()
                .block()?.results?.first() ?: throw ServiceException(ErrorCode.EXTERNAL_API_ERROR)
        } catch (e: Exception) {
            log.error("readPrevClose fail!! tickerCode = {}, error = {}", tickerCode, e.message)
        }
        return PolygonPrevClose("0.0")
    }

    fun readDailyClose(tickerCode: String, date: LocalDate): PolygonDailyClose {
        val dateString = date.toString()
        val url = String.format(PolygonConstant.POLYGON_DAILY_CLOSE_QUERY, tickerCode, dateString, apiKey)
        try {
            return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono<PolygonDailyClose>()
                .block() ?: throw ServiceException(ErrorCode.EXTERNAL_API_ERROR)
        } catch (e: Exception) {
            log.error("readDailClose fail!! tickerCode = {}, error = {}", tickerCode, e.message)
        }
        return PolygonDailyClose("0.0", dateString, tickerCode)
    }

}
