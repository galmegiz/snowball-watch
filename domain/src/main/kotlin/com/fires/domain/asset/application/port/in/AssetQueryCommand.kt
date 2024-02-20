package com.fires.domain.asset.application.port.`in`

import com.fires.common.constant.PageInfoConstant
import com.fires.domain.asset.constant.AssetCategoryType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min

/**
 * 자산 검색 명령
 */
data class AssetQueryCommand(
    @Schema(
        title = "자산 카테고리",
        description = "입력값이 없으면, 전체 카테고리 검색"
    )
    val category: AssetCategoryType? = null,

    @Schema(
        title = "검색어",
        description = "자산명, 티커코드, 종목코드 구분없이 검색 지원"
    )
    val word: String,

    @Schema(title = "페이지 인덱스")
    @Min(value = 1)
    val pageIndex: Int = PageInfoConstant.DEFAULT_PAGE_INDEX,

    @Schema(title = "페이지 크기")
    @Min(value = 1)
    val pageSize: Int = PageInfoConstant.DEFAULT_PAGE_SIZE
)
