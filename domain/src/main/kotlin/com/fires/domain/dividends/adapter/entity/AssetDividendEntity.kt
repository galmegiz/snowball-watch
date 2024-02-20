package com.fires.domain.dividends.adapter.entity

import com.fires.common.entity.BaseEntity
import com.fires.domain.asset.adapter.entity.AssetEntity
import com.fires.domain.asset.constant.FrequencyType
import com.fires.domain.asset.domain.entity.SimpleAssetDividend
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "asset_dividends")
class AssetDividendEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_dividend_id", nullable = false)
    val id: Long? = null,

    /**
     * 배당주기
     */
    @Enumerated(EnumType.STRING)
    val frequencyType: FrequencyType = FrequencyType.NONE,

    /**
     * 배당금
     */
    val dividend: BigDecimal = BigDecimal(0.0),

    /**
     * 배당락일
     */
    var exDividendDate: LocalDate? = null,

    /**
     * 배당일
     */
    var payDate: LocalDate? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    var assetEntity: AssetEntity? = null
) : BaseEntity() {
    fun toSimpleAssetDividend(): SimpleAssetDividend {
        return SimpleAssetDividend(
            id = this.id ?: 0,
            assetId = this.assetEntity?.id ?: 0,
            frequencyType = this.frequencyType,
            dividend = this.dividend,
            exDividendDate = this.exDividendDate,
            payDate = this.payDate
        )
    }
}
