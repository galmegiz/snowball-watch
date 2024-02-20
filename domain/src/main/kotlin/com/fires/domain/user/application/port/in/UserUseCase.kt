package com.fires.domain.user.application.port.`in`

import com.fires.domain.user.domain.dto.RecentSearchWord
import com.fires.domain.user.domain.dto.User
import com.fires.domain.user.domain.dto.UserLoginInfo
import com.fires.domain.user.domain.dto.UserSignUp
import java.time.LocalDateTime

interface UserUseCase {
    fun signUpUser(request: UserSignupCommand): UserSignUp

    fun checkUserRegistered(request: UserExistCheckCommand): Boolean

    fun loginUser(request: UserLoginCommand): UserLoginInfo
    fun logoutUser(jwtToken: String): LocalDateTime

    fun findUserByEmail(email: String): User?
    fun findUserById(userId: Long): User?
    fun readRecentSearchWords(email: String, size: Int): List<RecentSearchWord>
    fun clearAllRecentSearchWord(email: String): Boolean
    fun addRecentSearchWord(email: String, searchWord: String): Boolean
    fun deleteRecentSearchWord(email: String, searchWord: String): Boolean
    fun withdrawUser(email: String): Boolean
}
