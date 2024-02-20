package com.fires.api.portfolio

import com.fires.common.annotation.LoginUser
import com.fires.common.dto.CommonApiResponse
import com.fires.common.response.ErrorResponse
import com.fires.domain.portfolio.application.port.`in`.PortfolioAssetAddRequest
import com.fires.domain.portfolio.application.port.`in`.PortfolioAssetUpdateRequest
import com.fires.domain.portfolio.application.port.`in`.PortfolioManageUseCase
import com.fires.domain.portfolio.application.port.`in`.PortfolioReadUseCase
import com.fires.domain.portfolio.domain.dto.PortfolioAssetDeleteRequest
import com.fires.domain.portfolio.domain.dto.PortfolioAssetInfoRequest
import com.fires.domain.portfolio.domain.dto.SimplePortfolioAssetResponse
import com.fires.domain.portfolio.domain.dto.SimplePortfolioResponse
import com.fires.domain.portfolio.domain.entity.SimplePortfolioAsset
import com.fires.domain.user.domain.dto.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "포트폴리오")
@RestController
@RequestMapping("api/v1")
class PortfolioController(
    private val portfolioReadUseCase: PortfolioReadUseCase,
    private val portfolioManageUseCase: PortfolioManageUseCase
) {
    @Operation(summary = "포트폴리오 생성")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201"
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
    @PostMapping("/portfolio/create")
    fun createPortfolio(@LoginUser user: User) =
        CommonApiResponse(success = true, data = portfolioManageUseCase.createPortfolio(user.id))

    @Operation(summary = "포트폴리오 조회")
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
    @GetMapping("/portfolio/list")
    fun getPortfolio(@LoginUser user: User): CommonApiResponse<SimplePortfolioResponse> =
        CommonApiResponse(success = true, data = portfolioReadUseCase.getPortfolioWithSimpleAsset(user.id))

    @Operation(summary = "포트폴리오 자산 추가")
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
    @PostMapping("/portfolio/asset/add")
    fun addAssets(
        @LoginUser user: User,
        @Valid @RequestBody
        request: PortfolioAssetAddRequest
    ): CommonApiResponse<List<SimplePortfolioAsset>> {
        val result = portfolioManageUseCase.addAssets(request, user.id)
        return CommonApiResponse(
            success = result.size == request.assets.size,
            data = result
        )
    }
    @Operation(summary = "포트폴리오 자산 정보 확인")
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
    @GetMapping("/portfolio/asset")
    fun getPortfolioAsset(
        @LoginUser user: User,
        @ModelAttribute
        request: PortfolioAssetInfoRequest
    ): CommonApiResponse<SimplePortfolioAssetResponse> =
        CommonApiResponse(
            success = true,
            data = portfolioReadUseCase.getPortfolioAssetInfo(request, user.id)
        )

    @Operation(summary = "포트폴리오 자산 수정")
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
    @PostMapping("/portfolio/asset/update")
    fun updateAssets(
        @LoginUser user: User,
        @Valid @RequestBody
        request: PortfolioAssetUpdateRequest
    ): CommonApiResponse<List<SimplePortfolioAsset>> {
        val result = portfolioManageUseCase.updateAssets(request, user.id)
        return CommonApiResponse(
            success = result.size == request.assets.size,
            data = result
        )
    }

    @Operation(summary = "포트폴리오 자산 삭제")
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
    @DeleteMapping("/portfolio/asset/delete")
    fun deleteAsset(
        @LoginUser user: User,
        @Valid @RequestBody
        request: PortfolioAssetDeleteRequest
    ): CommonApiResponse<Boolean> = CommonApiResponse(success = portfolioManageUseCase.deleteAsset(request, user.id))

    @Operation(summary = "포트폴리오 삭제")
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
    @DeleteMapping("/portfolio")
    fun deletePortfolio(
        @LoginUser user: User,
        @RequestParam portfolioId: Long
    ): CommonApiResponse<Boolean> = CommonApiResponse(success = portfolioManageUseCase.deletePortfolio(portfolioId, user.id))
}
