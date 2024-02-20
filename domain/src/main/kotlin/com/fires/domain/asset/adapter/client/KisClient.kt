package com.fires.domain.asset.adapter.client

import com.fires.common.constant.CacheConstant
import com.fires.common.exception.ErrorCode
import com.fires.common.exception.ServiceException
import com.fires.common.logging.Log
import com.fires.domain.asset.adapter.dto.DomesticPriceResponse
import com.fires.domain.asset.adapter.dto.OverseasDailyPriceResponse
import com.fires.domain.asset.adapter.dto.OverseasPriceResponse
import com.fires.domain.asset.adapter.dto.TokenRequest
import com.fires.domain.asset.adapter.dto.TokenResponse
import com.fires.domain.asset.constant.AssetCategoryType
import com.fires.domain.asset.constant.AssetMarketType
import com.fires.domain.asset.constant.CountryType
import com.fires.domain.asset.constant.KisConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class KisClient(
    private val webClient: WebClient,
    @Value("\${external.kis.base-url}")
    private val baseUrl: String,
    @Value("\${external.kis.app-key}")
    private val appKey: String,
    @Value("\${external.kis.secret-key}")
    private val secretKey: String
) : Log {

    @Cacheable(cacheNames = [CacheConstant.KIS])
    fun getAccessToken(): TokenResponse = try {
        webClient
            .mutate()
            .baseUrl(baseUrl)
            .build()
            .post()
            .uri("/oauth2/tokenP")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TokenRequest(appKey, secretKey))
            .retrieve()
            .onStatus(
                { it.is4xxClientError || it.is5xxServerError },
                {
                    log.error("[Kis Api Exception] request method: POST, request uri: /oauth2/tokenP, error message: ${it.bodyToMono<String>()}")
                    throw ServiceException(ErrorCode.EXTERNAL_API_ERROR)
                }
            )
            .bodyToMono(TokenResponse::class.java)
            .block() ?: throw ServiceException(ErrorCode.EXTERNAL_API_ERROR)
    } catch (e: Exception) {
        log.error("get token from KIS fail !!")
        throw ServiceException(ErrorCode.EXTERNAL_API_ERROR)
    }

    // 국내 자산 현재가
    suspend fun getDomesticCurrentPrice(
        accessToken: String,
        stockCode: String,
        assetCategory: AssetCategoryType
    ): DomesticPriceResponse {
        val marketDivCode = when (assetCategory) {
            AssetCategoryType.ETF -> MarketDivCode.ETF
            AssetCategoryType.ETN -> MarketDivCode.ETN
            AssetCategoryType.STOCK -> MarketDivCode.J
        }

        return try {
            withContext(Dispatchers.IO) {
                webClient.mutate()
                    .baseUrl(baseUrl)
                    .build()
                    .get()
                    .uri {
                        it.path("/uapi/domestic-stock/v1/quotations/inquire-price")
                            .queryParam("FID_COND_MRKT_DIV_CODE", marketDivCode)
                            .queryParam("FID_INPUT_ISCD", stockCode)
                            .build()
                    }
                    .headers { it.addAll(createHeaderForCurrentPrice(accessToken, CountryType.KOR, KisConstant.DOMESTIC_ASSET_CURRENT_PRICE_ID)) }
                    .retrieve()
                    .onStatus(
                        { it.is4xxClientError || it.is5xxServerError },
                        {
                            log.error("[Kis Api Exception] request method: GET, request uri: /uapi/domestic-stock/v1/quotations/inquire-price, error message: ${it.bodyToMono<String>()}")
                            throw ServiceException(ErrorCode.EXTERNAL_API_ERROR)
                        }
                    )
                    .bodyToMono(DomesticPriceResponse::class.java)
                    .block()
            } ?: DomesticPriceResponse()
        } catch (e: Exception) {
            log.error("get {} domestic current price from KIS fail !!", stockCode, e)
            return DomesticPriceResponse()
        }
    }

    suspend fun getDomesticPriceOfDate(
        accessToken: String,
        stockCode: String,
        assetCategory: AssetCategoryType,
        date: LocalDate
    ): DomesticPriceResponse {
        val marketDivCode = when (assetCategory) {
            AssetCategoryType.ETF -> MarketDivCode.ETF
            AssetCategoryType.ETN -> MarketDivCode.ETN
            AssetCategoryType.STOCK -> MarketDivCode.J
        }

        return try {
            withContext(Dispatchers.IO) {
                webClient.mutate()
                    .baseUrl(baseUrl)
                    .build()
                    .get()
                    .uri {
                        it.path("/uapi/domestic-stock/v1/quotations/inquire-price")
                            .queryParam("FID_COND_MRKT_DIV_CODE", marketDivCode)
                            .queryParam("FID_INPUT_ISCD", stockCode)
                            .queryParam("FID_INPUT_DATE_1", date.format(DateTimeFormatter.ofPattern("YYYYMMDD")))
                            .queryParam("FID_INPUT_DATE_2", date.format(DateTimeFormatter.ofPattern("YYYYMMDD")))
                            .queryParam("FID_PERIOD_DIV_CODE", "D")
                            .queryParam("FID_ORG_ADJ_PRC", "0")
                            .build()
                    }
                    .headers { it.addAll(createHeaderForCurrentPrice(accessToken, CountryType.KOR, KisConstant.DOMESTIC_ASSET_CURRENT_PRICE_ID)) }
                    .retrieve()
                    .onStatus(
                        { it.is4xxClientError || it.is5xxServerError },
                        {
                            log.error("[Kis Api Exception] request method: GET, request uri: /uapi/domestic-stock/v1/quotations/inquire-price, error message: ${it.bodyToMono<String>()}")
                            throw ServiceException(ErrorCode.EXTERNAL_API_ERROR)
                        }
                    )
                    .bodyToMono(DomesticPriceResponse::class.java)
                    .block()
            } ?: DomesticPriceResponse()
        } catch (e: Exception) {
            log.error("get {} domestic price of date from KIS fail !!", stockCode, e)
            return DomesticPriceResponse()
        }
    }

    // 해외 자산 현재가
    suspend fun getOverseasCurrentPrice(
        accessToken: String,
        tickerCode: String,
        marketType: AssetMarketType
    ): OverseasPriceResponse {
        val exchangeCode = convertToExchangeCode(marketType, tickerCode)

        return try {
            withContext(Dispatchers.IO) {
                webClient.mutate()
                    .baseUrl(baseUrl)
                    .build()
                    .get()
                    .uri {
                        it.path("/uapi/overseas-price/v1/quotations/price")
                            .queryParam("AUTH", "")
                            .queryParam("EXCD", exchangeCode)
                            .queryParam("SYMB", tickerCode)
                            .build()
                    }
                    .headers { it.addAll(createHeaderForCurrentPrice(accessToken, CountryType.USA, KisConstant.OVERSEAS_ASSET_CURRENT_PRICE_ID)) }
                    .retrieve()
                    .onStatus(
                        { it.is4xxClientError || it.is5xxServerError },
                        {
                            log.error("[Kis Api Exception] request method: GET, request uri: /uapi/overseas-price/v1/quotations/price, error message: ${it.bodyToMono<String>()}")
                            throw ServiceException(ErrorCode.EXTERNAL_API_ERROR)
                        }
                    )
                    .bodyToMono(OverseasPriceResponse::class.java)
                    .block()
            } ?: OverseasPriceResponse()
        } catch (e: Exception) {
            log.error("get {} overseas price from KIS fail !!", tickerCode, e.message)
            return OverseasPriceResponse()
        }
    }

    suspend fun getOverseasPriceOfDate(
        accessToken: String,
        tickerCode: String,
        marketType: AssetMarketType,
        searchDate: LocalDate
    ): OverseasDailyPriceResponse {
        val exchangeCode = convertToExchangeCode(marketType, tickerCode)
        return try {
            withContext(Dispatchers.IO) {
                webClient.mutate()
                    .baseUrl(baseUrl)
                    .build()
                    .get()
                    .uri {
                        it.path("/uapi/overseas-price/v1/quotations/dailyprice")
                            .queryParam("AUTH", "")
                            .queryParam("EXCD", exchangeCode)
                            .queryParam("SYMB", tickerCode)
                            .queryParam("GUBN", KisConstant.GUBN_DAILY)
                            .queryParam("BYMD", searchDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .queryParam("MODP", "0")
                            .build()
                    }
                    .headers { it.addAll(createHeaderForCurrentPrice(accessToken, CountryType.USA, KisConstant.OVERSEAS_ASSET_PRICE_OF_DATE_ID)) }
                    .retrieve()
                    .onStatus(
                        { it.is4xxClientError || it.is5xxServerError },
                        {
                            log.error("[Kis Api Exception] request method: GET, request uri: /uapi/overseas-price/v1/quotations/dailyprice, error message: ${it.bodyToMono<String>()}")
                            throw ServiceException(ErrorCode.EXTERNAL_API_ERROR)
                        }
                    )
                    .bodyToMono(OverseasDailyPriceResponse::class.java)
                    .block()
            } ?: OverseasDailyPriceResponse()
        } catch (e: Exception) {
            log.error("get {} overseas price of date from KIS fail !!", tickerCode, e)
            return OverseasDailyPriceResponse()
        }
    }

    private fun convertToExchangeCode(
        marketType: AssetMarketType,
        tickerCode: String
    ) = when (marketType) {
        AssetMarketType.AMEX -> ExchangeCode.AMS
        AssetMarketType.NASDAQ -> ExchangeCode.NAS
        AssetMarketType.NYSE -> ExchangeCode.NYS
        else -> {
            log.error(
                "domestic market type in overseas api request. request = {}, marketType = {}",
                tickerCode,
                marketType
            )
            throw IllegalArgumentException("domestic market type in overseas api request")
        }
    }

    private fun createHeaderForCurrentPrice(accessToken: String, countryType: CountryType, transactionId: String): HttpHeaders {
        val httpHeaders = HttpHeaders()
        httpHeaders.add("authorization", "Bearer $accessToken")
        httpHeaders.add("tr_id", transactionId)
        httpHeaders.add("appkey", appKey)
        httpHeaders.add("appsecret", secretKey)
        return httpHeaders
    }

    enum class MarketDivCode {
        J, ETF, ETN
    }

    enum class ExchangeCode {
        NAS, NYS, AMS
    }
}
