package com.fires.api.asset

import com.fires.common.annotation.LoginUser
import com.fires.common.dto.CommonApiResponse
import com.fires.common.dto.PageResponse
import com.fires.common.response.ErrorResponse
import com.fires.domain.asset.application.port.`in`.AssetQueryCommand
import com.fires.domain.asset.application.port.`in`.AssetUseCase
import com.fires.domain.asset.domain.dto.CurrentPriceRequest
import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.asset.domain.entity.Price
import com.fires.domain.user.domain.dto.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "자산")
@RestController
@RequestMapping("api/v1")
@Validated
class AssetController(
    private val assetUseCase: AssetUseCase
) {
    @Operation(summary = "자산 목록 검색")
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
    @GetMapping("/asset/find")
    fun findAsset(@ModelAttribute request: AssetQueryCommand, @LoginUser user: User): PageResponse<Asset> {
        val result = assetUseCase.findAsset(request, user.email)
        return PageResponse(
            data = result.content,
            page = result.number,
            pageSize = result.size,
            next = result.hasNext()
        )
    }

    @Operation(summary = "자산 현재가 검색")
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
    // TODO GET 메소드를 사용하면서 argumentResolver를 사용하는 쪽으로 수정 필요
    @PostMapping("/asset/price")
    fun getCurrentPrice(@RequestBody currentPriceRequests: List<CurrentPriceRequest>): CommonApiResponse<List<Price>> {
        val result = assetUseCase.getAssetsCurrentPrice(currentPriceRequests)
        return CommonApiResponse(
            success = true,
            data = result
        )
    }
}
