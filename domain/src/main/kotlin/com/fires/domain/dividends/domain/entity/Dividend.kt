package com.fires.domain.dividends.domain.entity

import com.fires.domain.asset.constant.CountryType
import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.portfolio.domain.entity.SimplePortfolioAsset
import java.math.BigDecimal
import java.time.LocalDate

class Dividend(
    val assetId: Long,
    val ticker: String?,
    val stockCode: String?,
    var cashAmount: Double,
    val exDividendDate: LocalDate,
    val payDate: LocalDate
){
    fun toKRW(asset: Asset, exchangerRate: Double) {
        require(this.assetId == asset.assetId){"Asset id must be same"}
        if(asset.countryType == CountryType.KOR) return

        cashAmount *= exchangerRate
    }

    fun getTotalDividendCashAMount(userAsset: SimplePortfolioAsset): BigDecimal {
        return BigDecimal(userAsset.count * cashAmount)
    }
}

