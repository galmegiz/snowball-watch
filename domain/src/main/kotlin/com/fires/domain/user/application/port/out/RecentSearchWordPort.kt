package com.fires.domain.user.application.port.out

import com.fires.domain.user.domain.dto.RecentSearchWord

interface RecentSearchWordPort {
    fun readRecentSearchWords(email: String, size: Int): List<RecentSearchWord>

    fun addRecentSearchWord(email: String, searchWord: String): Boolean

    fun clearSearchWords(email: String): Boolean
    fun deleteRecentSearchWord(email: String, searchWord: String): Boolean
}
