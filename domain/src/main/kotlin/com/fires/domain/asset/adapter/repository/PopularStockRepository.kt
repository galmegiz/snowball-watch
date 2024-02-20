package com.fires.domain.asset.adapter.repository

import com.fires.domain.asset.adapter.entity.PopularStockEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PopularStockRepository : JpaRepository<PopularStockEntity, Long>
