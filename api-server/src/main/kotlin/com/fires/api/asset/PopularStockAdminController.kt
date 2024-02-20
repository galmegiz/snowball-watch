package com.fires.api.asset

import com.fires.common.dto.CommonApiResponse
import com.fires.common.response.ErrorResponse
import com.fires.domain.asset.application.port.`in`.PopularStockAdminUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "인기주식 어드민")
@RestController
@RequestMapping("api/v1")
@Validated
class PopularStockAdminController(
    private val popularStockAdminUseCase: PopularStockAdminUseCase
) {
    @Operation(summary = "인기주식 등록")
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
    @PostMapping("/popular-stock/admin/regist")
    fun init(@RequestBody name: String): CommonApiResponse<Boolean> =
        CommonApiResponse(success = popularStockAdminUseCase.registPopularStock(name))
}
