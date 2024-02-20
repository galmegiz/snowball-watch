package com.fires.domain.asset.adapter.dto

import com.fires.domain.asset.constant.AssetMarketType
import com.fires.domain.asset.constant.CountryType
import com.fires.domain.asset.constant.YfinacePeriodType
import com.fires.domain.asset.domain.entity.Asset
import java.time.LocalDate

class YfinaceRequest(asset: Asset,
                     targetDate: LocalDate){
    val symbol: String
    val periodType: YfinacePeriodType

    init{
        symbol = when(asset.countryType){
            CountryType.USA -> asset.tickerCode ?: throw IllegalArgumentException("Overseas asset's ticker code is null")
            CountryType.KOR -> {
                val stockCode = asset.stockCode ?: throw IllegalArgumentException("Domestic asset's stock code is null")
                val symbol = appendSuffixToStockCode(asset.marketType, stockCode)
                symbol
            }
        }

        periodType = YfinacePeriodType.from(targetDate)
    }

    private fun appendSuffixToStockCode(marketType: AssetMarketType, stockCode: String): String {
        val symbol = if (marketType == AssetMarketType.KRX_KOSDAQ) {
            "$stockCode.KQ"
        } else {
            "$stockCode.KS"
        }
        return symbol
    }
}
