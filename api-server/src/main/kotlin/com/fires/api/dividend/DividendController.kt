package com.fires.api.dividend

import com.fires.common.annotation.LoginUser
import com.fires.common.dto.CommonApiResponse
import com.fires.common.response.ErrorResponse
import com.fires.domain.dividends.application.port.`in`.DividendUseCase
import com.fires.domain.user.domain.dto.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1")
@Tag(name = "배당달력")
class DividendController(
    private val dividendUseCase: DividendUseCase
) {
    @Operation(summary = "이번달 배당정보")
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
    @GetMapping("/dividend/calendar")
    fun getDividendCalendar(
        @LoginUser user: User
    ) = CommonApiResponse(
        success = true,
        data = dividendUseCase.getDividendCalendar(user.id)
    )

    @Operation(summary = "연간 배당정보")
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
    @GetMapping("/dividend/annual")
    fun getTotalDividend(
        @LoginUser user: User
    ) = CommonApiResponse(
        success = true,
        data = dividendUseCase.getAnnualDividendInfo(user.id)
    )
}
