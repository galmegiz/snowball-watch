package com.fires.api.user

import com.fires.common.annotation.LoginUser
import com.fires.common.dto.CommonApiResponse
import com.fires.common.jwt.TokenHttpRequestUtil
import com.fires.common.response.ErrorResponse
import com.fires.domain.user.application.port.`in`.UserExistCheckCommand
import com.fires.domain.user.application.port.`in`.UserLoginCommand
import com.fires.domain.user.application.port.`in`.UserSignupCommand
import com.fires.domain.user.application.port.`in`.UserUseCase
import com.fires.domain.user.constant.OAuthChannelType
import com.fires.domain.user.constant.UserConstant.DEFAULT_SEARCH_WORD_SIZE
import com.fires.domain.user.domain.dto.RecentSearchWord
import com.fires.domain.user.domain.dto.User
import com.fires.domain.user.domain.dto.UserLoginInfo
import com.fires.domain.user.domain.dto.UserSignUp
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@Tag(name = "유저")
@RestController
@RequestMapping("api/v1")
@Validated
class UserController(
    private val userUseCase: UserUseCase,
    private val tokenUtil: TokenHttpRequestUtil
) {
    @Operation(summary = "회원 가입")
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
    @PostMapping("/user/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(
        @Valid @RequestBody
        request: UserSignupCommand
    ): CommonApiResponse<UserSignUp> = CommonApiResponse(success = true, data = userUseCase.signUpUser(request))

    @Operation(summary = "회원 가입 전 이메일 중복 여부 확인")
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
    @GetMapping("/user/email")
    fun checkUserRegistered(
        @RequestParam(name = "email") email: String,
        @RequestParam(name = "oAuthChannelType") oAuthChannelType: OAuthChannelType
    ) = userUseCase.checkUserRegistered(
        UserExistCheckCommand(
            email = email,
            oAuthChannelType = oAuthChannelType
        )
    )

    @Operation(summary = "회원 로그인")
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
    @PostMapping("/user/login")
    fun userLogin(
        @Valid @RequestBody
        request: UserLoginCommand
    ): CommonApiResponse<UserLoginInfo> =
        CommonApiResponse(success = true, data = userUseCase.loginUser(request))

    @Operation(summary = "회원 로그아웃")
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
    @PostMapping("/user/logout")
    fun userLogout(httpRequest: HttpServletRequest): CommonApiResponse<LocalDateTime> {
        val jwtToken = tokenUtil.getToken(httpRequest)
        return CommonApiResponse(
            success = true,
            data = userUseCase.logoutUser(jwtToken)
        )
    }

    @Operation(summary = "회원 탈퇴")
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
    @DeleteMapping("/user")
    fun withdraw(@LoginUser user: User): CommonApiResponse<Unit>
        = CommonApiResponse(success = userUseCase.withdrawUser(user.email))

    @Operation(
        summary = "회원 최근 검색어 검색",
        description = "현재 로그인 회원 정보 기반으로 최근 검색어 검색"
    )
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
    @GetMapping("/user/recent-search-word/list")
    fun readRecentSearchWords(@LoginUser user: User, @RequestParam(required = false, defaultValue = DEFAULT_SEARCH_WORD_SIZE.toString()) size: Int):
        CommonApiResponse<List<RecentSearchWord>> =
        CommonApiResponse(success = true, data = userUseCase.readRecentSearchWords(user.email, size))

    @Operation(summary = "최근 검색어 초기화")
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
    @PostMapping("/user/recent-search-word/clear-all")
    fun userLogin(@LoginUser user: User): CommonApiResponse<Boolean> =
        CommonApiResponse(success = userUseCase.clearAllRecentSearchWord(user.email))

    @Operation(summary = "최근 검색어  추가")
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
    @PostMapping("/user/recent-search-word")
    fun addRecentSearchWord(
        @LoginUser user: User,
        @RequestParam searchWord: String
    ): CommonApiResponse<Boolean> = CommonApiResponse(success = userUseCase.addRecentSearchWord(user.email, searchWord))

    @Operation(summary = "최근 검색어  삭제(특정 검색어)")
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
    @DeleteMapping("/user/recent-search-word")
    fun deleteSearchWord(
        @LoginUser user: User,
        @RequestParam searchWord: String
    ): CommonApiResponse<Boolean> = CommonApiResponse(success = userUseCase.deleteRecentSearchWord(user.email, searchWord))
}
