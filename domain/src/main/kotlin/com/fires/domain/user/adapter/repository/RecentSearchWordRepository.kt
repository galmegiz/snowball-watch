package com.fires.domain.user.adapter.repository

import com.fires.domain.user.adapter.entity.RecentSearchWordEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface RecentSearchWordRepository : JpaRepository<RecentSearchWordEntity, Long> {
    fun findRecentSearchWordEntitiesByEmail(email: String, pageable: Pageable): List<RecentSearchWordEntity>
    fun deleteAllByEmail(email: String): Long
    fun deleteByEmailAndSearchWord(email: String, searchWord: String): Long
}
