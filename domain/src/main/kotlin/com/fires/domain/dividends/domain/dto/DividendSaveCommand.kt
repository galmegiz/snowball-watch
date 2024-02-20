package com.fires.domain.dividends.domain.dto

import com.fires.domain.asset.adapter.entity.AssetEntity
import com.fires.domain.dividends.domain.entity.Dividend

data class DividendSaveCommand(
    val assetEntity: AssetEntity,
    val dividend: Dividend
) {
}