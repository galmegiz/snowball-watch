package com.fires.domain.portfolio.application.port.`in`

import com.fires.common.constant.CurrencyType
import com.fires.domain.portfolio.application.port.out.PortfolioAssetUpdateCommand
import io.swagger.v3.oas.annotations.media.Schema
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

@Schema(name = "포트폴리오 자산 업데이트")
data class AssetUpdateRequest(
    @Schema(title = "포트폴리오 자산 id")
    @NotNull
    val portfolioAssetId: Long,
    @Schema(title = "자산 구매가격")
    val price: BigDecimal,
    @Schema(title = "자산 수량")
    val count: Double,
    @Schema(title = "통화")
    val currencyType: CurrencyType = CurrencyType.KRW,
    @Schema(title = "순서")
    val priority: Int
) {
    fun toPortfolioAssetUpdateCommand() = PortfolioAssetUpdateCommand(
        portfolioAssetId = this.portfolioAssetId,
        purchasePrice = this.price,
        count = this.count,
        currencyType = this.currencyType,
        order = this.priority
    )
}
