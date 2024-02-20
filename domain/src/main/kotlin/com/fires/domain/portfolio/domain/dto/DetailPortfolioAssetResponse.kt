package com.fires.domain.portfolio.domain.dto

import com.fires.common.constant.CurrencyType
import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.asset.domain.entity.Price
import com.fires.domain.portfolio.domain.entity.SimplePortfolioAsset
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.math.RoundingMode

@Schema(name = "포트폴리오 자산 상세")
data class DetailPortfolioAssetResponse(
    @Schema(title = "포트폴리오 자산 id")
    val portfolioAssetId: Long,
    @Schema(title = "자산 id")
    val assetId: Long,
    @Schema(title = "자산 이름")
    val name: String,
    @Schema(title = "티커")
    val tickerCode: String? = null,
    @Schema(title = "주식코드")
    val stockCode: String? = null,
    @Schema(title = "자산수량")
    val count: Double,
    @Schema(title = "평단가")
    var averagePrice: BigDecimal,
    @Schema(title = "현재가")
    var currentPrice: BigDecimal,
    @Schema(title = "등략율")
    var assetPriceChangeRate: BigDecimal,
    @Schema(title = "등략")
    var assetPriceChange: BigDecimal,
    @Schema(title = "자산가치")
    var value: BigDecimal,
    @Schema(title = "수익률")
    var rateOfReturn: BigDecimal,
    @Schema(title = "배당수익률")
    var dividendPriceRatio: BigDecimal,
    @Schema(title = "배당주기")
    val dividendMonth: Collection<Int>,
    @Schema(title = "통화")
    val currencyType: CurrencyType,
    @Schema(title = "통화")
    val priority: Int = 100
) {
    init {
        this.averagePrice = this.averagePrice.setScale(2, RoundingMode.HALF_UP)
        this.currentPrice = this.currentPrice.setScale(2, RoundingMode.HALF_UP)
        this.assetPriceChangeRate = this.assetPriceChangeRate.setScale(2, RoundingMode.HALF_UP)
        this.assetPriceChange = this.assetPriceChange.setScale(2, RoundingMode.HALF_UP)
        this.value = this.value.setScale(2, RoundingMode.HALF_UP)
        this.rateOfReturn = this.rateOfReturn.setScale(2, RoundingMode.HALF_UP)
        this.dividendPriceRatio = this.dividendPriceRatio.setScale(2, RoundingMode.HALF_UP)
    }

    constructor(
        asset: Asset,
        portfolioAsset: SimplePortfolioAsset,
        price: Price,
        assetValue: BigDecimal,
        rateOfReturn: BigDecimal,
        dividendPriceRatio: BigDecimal,
        dividendMonth: Collection<Int>) : this(
        portfolioAssetId = portfolioAsset.portfolioAssetId,
        assetId = asset.assetId,
        name = asset.name,
        tickerCode = asset.tickerCode,
        stockCode = asset.stockCode,
        count = portfolioAsset.count,
        averagePrice = portfolioAsset.purchasePrice,
        currentPrice = price.currentPrice,
        assetPriceChangeRate = price.priceChangeRate,
        assetPriceChange = price.priceChange,
        value = assetValue,
        rateOfReturn = rateOfReturn,
        dividendPriceRatio = dividendPriceRatio,
        dividendMonth = dividendMonth,
        currencyType = CurrencyType.KRW,
        priority = portfolioAsset.priority
    )



}

