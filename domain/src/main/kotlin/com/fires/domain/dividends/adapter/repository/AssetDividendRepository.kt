package com.fires.domain.dividends.adapter.repository

import com.fires.domain.dividends.adapter.entity.AssetDividendEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface AssetDividendRepository : JpaRepository<AssetDividendEntity, Long> {
    fun findByIdInAndPayDateBetween(assetIds: Collection<Long>, startOfMonth: LocalDate, endOfMonth: LocalDate): List<AssetDividendEntity>
}
