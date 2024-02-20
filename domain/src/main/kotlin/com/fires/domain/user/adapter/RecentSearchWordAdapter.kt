package com.fires.domain.user.adapter

import com.fires.common.logging.Log
import com.fires.domain.user.adapter.entity.RecentSearchWordEntity
import com.fires.domain.user.adapter.repository.RecentSearchWordRepository
import com.fires.domain.user.application.port.out.RecentSearchWordPort
import com.fires.domain.user.domain.dto.RecentSearchWord
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class RecentSearchWordAdapter(
    private val recentSearchWordRepository: RecentSearchWordRepository
) : RecentSearchWordPort, Log {

    @Transactional(readOnly = true)
    override fun readRecentSearchWords(email: String, size: Int): List<RecentSearchWord> =
        recentSearchWordRepository.findRecentSearchWordEntitiesByEmail(email, PageRequest.of(0, size))
            .map { it.toRecentSearchWord() }

    @Transactional
    override fun addRecentSearchWord(email: String, searchWord: String): Boolean {
        val entity = RecentSearchWordEntity(email = email, searchWord = searchWord)
        recentSearchWordRepository.save(entity)
        return entity.id != null
    }

    /**
     * return 모든 데이터를 삭제하므로 검증 불필요, 쿼리가 정상실행만 됐다면 true반환
     */
    @Transactional
    override fun clearSearchWords(email: String): Boolean {
        return recentSearchWordRepository.deleteAllByEmail(email) >= 0
    }

    /**
     * return 삭제된 데이터가 존재하면 true, 아니라면 false
     */
    @Transactional
    override fun deleteRecentSearchWord(email: String, searchWord: String): Boolean {
        return recentSearchWordRepository.deleteByEmailAndSearchWord(email, searchWord) > 0
    }
}
