package com.fires.domain.user.domain.service

import com.fires.common.ampitude.constant.UserEventType
import com.fires.common.ampitude.event.UserEvent
import com.fires.common.auth.helper.jwt.JwtUtils
import com.fires.common.constant.CacheConstant
import com.fires.common.exception.ErrorCode
import com.fires.common.exception.ServiceException
import com.fires.common.logging.Log
import com.fires.domain.auth.domain.dto.Login
import com.fires.domain.user.application.port.`in`.UserExistCheckCommand
import com.fires.domain.user.application.port.`in`.UserLoginCommand
import com.fires.domain.user.application.port.`in`.UserSignupCommand
import com.fires.domain.user.application.port.`in`.UserUseCase
import com.fires.domain.user.application.port.out.RecentSearchWordPort
import com.fires.domain.user.application.port.out.UserCreateCommand
import com.fires.domain.user.application.port.out.UserPort
import com.fires.domain.user.constant.UserConstant.DEFAULT_SEARCH_WORD_SIZE
import com.fires.domain.user.constant.UserConstant.MAX_SEARCH_WORD_SIZE
import com.fires.domain.user.domain.dto.RecentSearchWord
import com.fires.domain.user.domain.dto.User
import com.fires.domain.user.domain.dto.UserLoginInfo
import com.fires.domain.user.domain.dto.UserSignUp
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@CacheConfig(cacheNames = ["default"])
@Service
class UserService(
    private val userPort: UserPort,
    private val recentSearchWordPort: RecentSearchWordPort,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtils: JwtUtils,
    private val eventPublisher: ApplicationEventPublisher
) : Log, UserUseCase {
    @Transactional
    override fun signUpUser(request: UserSignupCommand): UserSignUp {
        userPort.findValidUserByEmail(request.email)?.let {
            throw ServiceException(errorCode = ErrorCode.USER_EXISTS)
        }

        val password = passwordEncoder.encode(request.password)

        val userCreateCommand = UserCreateCommand(
            password = password,
            userName = request.userName,
            email = request.email,
            oAuthChannelType = request.oAuthChannelType
        )

        val user = userPort.save(userCreateCommand)
            ?: throw ServiceException(errorCode = ErrorCode.USER_EXISTS)
        val token = jwtUtils.generateToken(user.userType.name, user.id, user.email)
        eventPublisher.publishEvent(UserEvent(this, user.email, UserEventType.SEVER_SIGN_UP))
        return UserSignUp(
            login = Login(
                userType = user.userType,
                token = token
            )
        )
    }

    override fun findUserByEmail(email: String): User? = userPort.findValidUserByEmail(email)
    override fun findUserById(userId: Long): User? = userPort.findValidUserById(userId)
    override fun readRecentSearchWords(email: String, size: Int): List<RecentSearchWord> {
        val validSize = when (size) {
            in 1..MAX_SEARCH_WORD_SIZE -> size
            else -> DEFAULT_SEARCH_WORD_SIZE
        }
        return recentSearchWordPort.readRecentSearchWords(email, validSize)
    }

    override fun clearAllRecentSearchWord(email: String): Boolean {
        return recentSearchWordPort.clearSearchWords(email)
    }

    @Transactional(readOnly = true)
    override fun checkUserRegistered(request: UserExistCheckCommand): Boolean {
        return userPort.findValidUserByEmailAndOauthType(request.email, request.oAuthChannelType)
            ?.let { true }
            ?: false
    }

    @Transactional
    override fun loginUser(request: UserLoginCommand): UserLoginInfo {
        val user = userPort.findValidUserByEmailAndOauthType(request.email, request.oAuthChannelType) ?: let {
            throw ServiceException(errorCode = ErrorCode.USER_NOT_FOUND)
        }

        // 비밀번호 검증은 현재 필요없는 로직이므로 or 조건으로 삭제여부만 판별
/*        if (!passwordEncoder.matches(request.password, user.password) || user.isWithdrawal()) {
            throw ServiceException(ErrorCode.BAD_CREDENTIALS_ERROR)
        }*/

        userPort.updateLastLoginTime(user)
        val token = jwtUtils.generateToken(user.userType.name, user.id, user.email)
        eventPublisher.publishEvent(UserEvent(this, user.email, UserEventType.SEVER_SIGN_IN))
        return UserLoginInfo(
            login = Login(
                userType = user.userType,
                token = token
            )
        )
    }

    // key : value = 사용자 token : 로그아웃 시간
    @Cacheable(value = [CacheConstant.LOGOUT_USER_TOKEN])
    override fun logoutUser(jwtToken: String): LocalDateTime = LocalDateTime.now()

    override fun addRecentSearchWord(email: String, searchWord: String): Boolean =
        recentSearchWordPort.addRecentSearchWord(email, searchWord)

    override fun deleteRecentSearchWord(email: String, searchWord: String): Boolean =
        recentSearchWordPort.deleteRecentSearchWord(email, searchWord)

    override fun withdrawUser(email: String): Boolean {
        val result = userPort.deleteUserByEmail(email)
        eventPublisher.publishEvent(UserEvent(this, email, UserEventType.SERVER_WITHDRAW))
        return result
    }
}
