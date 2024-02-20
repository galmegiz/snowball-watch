package com.fires.domain.user.domain.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 * 최근 검색어
 */
@Schema(name = "최근 검색어")
data class RecentSearchWord(
    @Schema(title = "검색어")
    val word: String,

    @Schema(title = "검색날짜")
    val date: LocalDateTime
)
