package com.fires.api.asset

import com.fires.common.response.ErrorResponse
import com.fires.domain.asset.application.port.`in`.AssetAdminUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "자산 어드민")
@RestController
@RequestMapping("api/v1")
@Validated
class AssetAdminController(
    private val assetAdminUseCase: AssetAdminUseCase
) {
    @Operation(summary = "자산 데이터 초기화")
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
    @PostMapping("/asset/admin/init")
    fun init() = assetAdminUseCase.initAsset()
}
