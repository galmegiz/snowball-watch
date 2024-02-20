package com.fires.domain.asset.adapter.entity

import com.fires.common.entity.BaseEntity
import com.fires.domain.asset.domain.dto.PopulorStock
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "popular_stocks")
class PopularStockEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "popular_stock_id", nullable = false)
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
    @Column(unique = true)
    val name: String
) : BaseEntity() {
    fun toPopulorStock(): PopulorStock = PopulorStock(
        tickerCode = this.tickerCode,
        stockCode = this.stockCode,
        name = this.name
    )
}
