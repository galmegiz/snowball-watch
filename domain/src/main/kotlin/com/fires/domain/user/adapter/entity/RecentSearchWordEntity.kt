package com.fires.domain.user.adapter.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fires.domain.user.domain.dto.RecentSearchWord
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "recent_search_words")
class RecentSearchWordEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recent_search_word_id", nullable = false)
    val id: Long? = null,

    /**
     * 검색어
     */
    var searchWord: String,

    /**
     * 이메일
     */
    val email: String,

    /**
     * 검색 일자
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    fun toRecentSearchWord() = RecentSearchWord(
        word = this.searchWord,
        date = this.createdAt
    )
}
