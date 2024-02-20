package com.fires.domain.asset.application.port.out

import com.fires.domain.asset.constant.AssetCategoryType
import com.fires.domain.asset.constant.AssetMarketType
import com.fires.domain.asset.constant.CountryType

/**
 * 자산 카테고리 등록 요청
 */
data class CategoryCreateCommand(
    val countryType: CountryType,
    val marketType: AssetMarketType = AssetMarketType.UNKNOWN,
    val assetCategoryType: AssetCategoryType
)
