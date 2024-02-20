package com.fires.domain.portfolio.application.port.`in`

import com.fires.common.constant.CurrencyType
import com.fires.domain.portfolio.application.port.out.PortfolioAssetAddCommand
import io.swagger.v3.oas.annotations.media.Schema
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

@Schema(name = "포트폴리오 추가 자산")
data class AssetAddRequest(
    @Schema(title = "자산 id")
    @NotNull
    val assetId: Long,
    @Schema(title = "자산 구매가격")
    val price: BigDecimal,
    @Schema(title = "자산 수량")
    val count: Double,
    @Schema(title = "통화")
    val currencyType: CurrencyType = CurrencyType.USD
) {
    fun toPortfolioAssetAddCommand(priority: Int) = PortfolioAssetAddCommand(
        assetId = this.assetId,
        purchasePrice = this.price,
        count = this.count,
        currencyType = this.currencyType,
        priority = priority
    )
}
