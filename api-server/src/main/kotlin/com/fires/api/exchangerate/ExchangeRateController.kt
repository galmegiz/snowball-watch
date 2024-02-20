package com.fires.api.exchangerate

import com.fires.common.dto.CommonApiResponse
import com.fires.common.response.ErrorResponse
import com.fires.domain.exchangerate.application.port.`in`.ExchangeRateUseCase
import com.fires.domain.exchangerate.domain.dto.ExchangeRate
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "환율")
@RestController
@RequestMapping("api/v1")
@Validated
class ExchangeRateController(
    private val exchangeRateUseCase: ExchangeRateUseCase
) {
    @Operation(summary = "환율 정보 조회")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200"
            ),
            ApiResponse(
                responseCode = "422",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "500",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping("/exchange-rate")
    fun getCurrentPrice(): CommonApiResponse<ExchangeRate> {
        return CommonApiResponse(
            success = true,
            data = exchangeRateUseCase.getExchangeRate()
        )
    }
}
