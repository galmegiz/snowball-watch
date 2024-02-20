package com.fires.domain.asset.adapter.repository

import com.fires.domain.asset.adapter.entity.AssetEntity
import com.fires.domain.asset.constant.AssetMarketType
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface AssetRepository : JpaRepository<AssetEntity, Long> {
    /**
     * @param tickerCode 자산 symbol(티커, 코드)로 찾기 위한 파라미터
     * @param stockCode 주식코드
     * @param name 자산 이름으로 찾기 위한 파라미터
     * @param pageable 페이징처리를 위한 조건, 프로젝트에서는 size=30으로 고정
     * @return 페이징 처리를 위해 slice 객체 반환
     */
    @Query(
        "SELECT a FROM AssetEntity a WHERE a.tickerCode like %:tickerCode% OR a.stockCode like %:stockCode% OR a.name like %:name% " +
                "ORDER BY " +
                "CASE " +
                "WHEN a.tickerCode = :tickerCode THEN 0 " +
                "WHEN a.stockCode = :stockCode THEN 1 " +
                "WHEN a.name = :name THEN 2 " +
                "WHEN a.tickerCode like %:tickerCode THEN 3 " +
                "WHEN a.tickerCode like :tickerCode% THEN 4 " +
                "WHEN a.tickerCode like %:tickerCode% THEN 5 " +
                "WHEN a.stockCode like %:stockCode THEN 6 " +
                "WHEN a.stockCode like :stockCode% THEN 7 " +
                "WHEN a.stockCode like %:stockCode% THEN 8 " +
                "WHEN a.name like %:name THEN 9 " +
                "WHEN a.name like :name% THEN 10 " +
                "WHEN a.name like %:name% THEN 11 " +
                "ELSE 12" +
                "END"
    )
    fun findByTickerCodeContainsIgnoreCaseOrStockCodeContainsIgnoreCaseOrNameContainingIgnoreCaseOrderByTickerCodeAsc(
        tickerCode: String,
        stockCode: String,
        name: String,
        pageable: Pageable
    ): Slice<AssetEntity>

    /**
     *
     * @param assetIds 검색하고자 하는 자산 id(애플리케이션에서 부여한 id)
     * @return 자산 리스트 반환
     */
    fun findByIdIn(assetIds: Collection<Long>): List<AssetEntity>
    fun findByTickerCode(tickerCode: String): AssetEntity

    @Query("select AssetEntity from AssetEntity asset inner join asset.assetDividendEntities assetDividend " +
            "where asset.id in :assetIds and assetDividend.payDate between :from and :to")
    fun findByAssetByDividendPeriod(assetIds: List<Long>, from: LocalDate, to: LocalDate): List<AssetEntity>

    fun findByMarketType(marketType: AssetMarketType, pageable: Pageable): List<AssetEntity>
}
