package com.fires.domain.asset.domain.entity

import com.fires.common.constant.CurrencyType
import com.fires.common.util.BigDecimalUtil
import com.fires.domain.asset.constant.KisConstant
import com.fires.domain.asset.domain.dto.DailyClosePrice
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDateTime

@Schema(name = "자산 현재가")
data class Price(
    @Schema(title = "자산 아이디")
    val assetId: Long,

    @Schema(title = "자산 현재가")
    var currentPrice: BigDecimal,

    @Schema(title = "통화")
    var currencyType: CurrencyType,

    @Schema(title = "현재가 검색 시간")
    val accessTime: LocalDateTime,

    @Schema(title = "대비기호(등락여부)")
    var sign: String?,

    @Schema(title = "전일 종가와 당일 현재가의 차이")
    var priceChange: BigDecimal,

    @Schema(title = "등락율")
    var priceChangeRate: BigDecimal
) {
    companion object{
        fun createEmptyPrice(asset: Asset): Price {
            return Price(assetId = asset.assetId,
                currentPrice = BigDecimal.ZERO,
                currencyType = CurrencyType.fromAssetCountry(asset.countryType),
                accessTime = LocalDateTime.now(),
                sign = "0",
                priceChange = BigDecimal.ZERO,
                priceChangeRate = BigDecimal.ZERO)
        }
    }

    fun changeToTargetCurrencyType(targetCurrencyType: CurrencyType, exchangeRate: Double) {
        if(targetCurrencyType == this.currencyType) return

        this.currencyType = targetCurrencyType
        when(targetCurrencyType){
            // 미화 -> 원화
            CurrencyType.KRW -> {
                val usdExchangeRate = exchangeRate.toBigDecimal()
                this.currentPrice = this.currentPrice.multiply(usdExchangeRate)
                this.priceChange = this.priceChange.multiply(usdExchangeRate)
            }

            // 원화 -> 미화
            CurrencyType.USD -> {
                val usdExchangeRate = exchangeRate.toBigDecimal()
                this.currentPrice = BigDecimalUtil.divideRoundToTwoDecimal(this.currentPrice, usdExchangeRate)
                this.priceChange = BigDecimalUtil.divideRoundToTwoDecimal(this.priceChange, usdExchangeRate)
            }
            else -> {}
        }
    }

    fun updatePriceChange(prevPrice: DailyClosePrice) {
        priceChange = currentPrice - prevPrice.closePrice
        priceChangeRate = if (prevPrice.closePrice == BigDecimal.ZERO) {
            BigDecimal.ZERO
        } else {
            // (해당일 종가 - 전일 종가)  / 해당일 종가 * 100
            BigDecimalUtil.calculatePercentChange(priceChange, prevPrice.closePrice)
        }
        sign = checkSign(prevPrice.closePrice)
    }

    fun getCurrentValue(count: Double): BigDecimal {
        return this.currentPrice * count.toBigDecimal()
    }

    private fun checkSign(priceChange: BigDecimal): String {
        val compare = priceChange.compareTo(BigDecimal(0))

        val sign = if (compare < 0) {
            KisConstant.DECLINE
        } else if (compare == 0) {
            KisConstant.CONSOLIDATION
        } else {
            KisConstant.INCREASE
        }
        return sign
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Price

        return assetId == other.assetId
    }

    override fun hashCode(): Int {
        return assetId.hashCode()
    }
}
