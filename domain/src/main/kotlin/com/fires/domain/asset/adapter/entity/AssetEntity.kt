package com.fires.domain.asset.adapter.entity

import com.fires.common.entity.BaseEntity
import com.fires.domain.asset.constant.AssetCategoryType
import com.fires.domain.asset.constant.AssetMarketType
import com.fires.domain.asset.constant.CountryType
import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.dividends.adapter.entity.AssetDividendEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "assets")
class AssetEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id", nullable = false)
    val id: Long? = null,

    /**
     * 티커코드
     * 미국 주식만 제공하는 코드
     * ex. 애플(AAPL), 쿠팡(CPNG)
     */
    val tickerCode: String? = null,
    /**
     * 한국 종목코드
     * 단축코드는 4자리로 구성되며 주권은 0 ~4로 시작, ETN은 5~8로 시작
     */
    val stockCode: String? = null,
    /**
     * 주식명
     */
    val name: String,

    /**
     * 국가 타입
     */
    @Enumerated(EnumType.STRING)
    var countryType: CountryType,

    /**
     * 거래소 : KRX, KRX_KOSPI, KRX_KOSDAQ, KRX_KONEX, NYSE, AMEX, NASDAQ, UNKNOWN
     *         미국 etf의 경우 거래소별로 분류된 정보가 없어서 초기에는 UNKNOWN으로 지정된다.
     *         이후 한국투자증권 API검색 과정에서 거래소가 식별되면 업데이트 된다.
     *         한국 ETF의 경우 KRX로 설정된다.
     */
    @Enumerated(EnumType.STRING)
    var marketType: AssetMarketType,

    /**
     * 자산의 유형
     */
    @Enumerated(EnumType.STRING)
    val assetCategoryType: AssetCategoryType
) : BaseEntity() {
    @OneToMany(mappedBy = "assetEntity", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var assetDividendEntities: MutableList<AssetDividendEntity> = mutableListOf()

    fun addPortfolioAssetEntity(entity: AssetDividendEntity) {
        this.assetDividendEntities.add(entity)
        entity.assetEntity = this
    }

    fun addPortfolioAssetEntityList(entityList: List<AssetDividendEntity>) {
        this.assetDividendEntities.addAll(entityList)
        entityList.forEach { it.assetEntity = this }
    }

    fun toAsset(): Asset = Asset(
        assetId = this.id ?: 0,
        tickerCode = this.tickerCode,
        stockCode = this.stockCode,
        name = this.name,
        countryType = this.countryType,
        marketType = this.marketType,
        assetCategoryType = this.assetCategoryType
    )
}
