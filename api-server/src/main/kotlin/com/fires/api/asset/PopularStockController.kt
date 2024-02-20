package com.fires.api.asset

import com.fires.common.response.ErrorResponse
import com.fires.domain.asset.application.port.`in`.PopularStockUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "인기주식")
@RestController
@RequestMapping("api/v1")
@Validated
class PopularStockController(
    private val popularStockUseCase: PopularStockUseCase
) {
    @Operation(summary = "인기주식 목록 조회")
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
    @GetMapping("/popular-stock/list")
    fun readPopularStocks(@RequestParam size: Int) = popularStockUseCase.readPopularStocks(size)
}
