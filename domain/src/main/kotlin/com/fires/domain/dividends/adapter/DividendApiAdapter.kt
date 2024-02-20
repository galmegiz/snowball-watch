package com.fires.domain.dividends.adapter

import com.fires.common.logging.Log
import com.fires.domain.asset.adapter.client.YfinaceClient
import com.fires.domain.asset.adapter.dto.YfinaceRequest
import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.dividends.application.port.out.DividendApiPort
import com.fires.domain.dividends.domain.entity.Dividend
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.time.YearMonth

@Component
class DividendApiAdapter(
    private val yfinaceClient: YfinaceClient
) : DividendApiPort, Log {

    // TODO 캐시보다는 db에 담는 쪽으로 변경
    // @Cacheable(cacheNames = [CacheConstant.DIVIDEND], key = "#asset.assetId")
    @CircuitBreaker(name = "queryDividends", fallbackMethod = "fallbackQueryDividends")
    override fun queryDividends(asset: Asset, from: YearMonth): List<Dividend> {
        return dividendFromYfinace(asset, from)
    }

    // TODO 캐시보다는 db에 담는 쪽으로 변경
    // @Cacheable(cacheNames = [CacheConstant.DIVIDEND_THIS_MONTH], key = "#asset.assetId")
    @CircuitBreaker(name = "queryDividends", fallbackMethod = "fallbackQueryDividends")
    override fun queryDividendsThisMonth(asset: Asset, from: YearMonth): List<Dividend> {
        return dividendFromYfinace(asset, from).filter{YearMonth.from(it.payDate).month == from.month}
    }

    private fun dividendFromYfinace(asset: Asset, from: YearMonth): List<Dividend> {
        val request = YfinaceRequest(asset, LocalDate.now())
        // TODO 리팩토링 필요
        var result = yfinaceClient.getDividends(request).toDividend(asset)
        if (result.size > 20) {
            result = result.subList(result.size - 20, result.size)
        }
        return result.filter { Year.from(it.payDate) >= Year.now().minusYears(1) }
            .sortedBy { it.payDate }
            .associateBy { Month.from(it.payDate) }
            .values.toList()
    }

    private fun fallbackQueryDividends(asset: Asset, from: YearMonth, t: Throwable): List<Dividend> {
        log.error("queryDividends fail!! symbol : {}, from: {}", asset.tickerCode ?: asset.stockCode, from)
        return emptyList()
    }
}
