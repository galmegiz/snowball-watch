package com.fires.domain.user.adapter

import com.fires.common.exception.ErrorCode
import com.fires.common.exception.ServiceException
import com.fires.common.logging.Log
import com.fires.domain.user.adapter.entity.UserEntity
import com.fires.domain.user.adapter.repository.UserRepository
import com.fires.domain.user.application.port.out.UserCreateCommand
import com.fires.domain.user.application.port.out.UserPort
import com.fires.domain.user.constant.OAuthChannelType
import com.fires.domain.user.domain.dto.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class UserAdapter(
    private val userRepository: UserRepository
) : UserPort, Log {
    @Transactional
    override fun save(command: UserCreateCommand): User? {
        val userEntity = UserEntity(
            userName = command.userName,
            oAuthChannelType = command.oAuthChannelType,
            password = command.password,
            userType = command.userType,
            email = command.email,
            lastLoginAt = LocalDateTime.now()
        )
        return userRepository.save(userEntity).toUser()
    }

    @Transactional(readOnly = true)
    override fun findEntityById(id: Long): UserEntity? {
        val userEntity = userRepository.findByIdOrNull(id)
        // soft 삭제 시 회원 조회 안됨
        return when (userEntity?.deletedAt) {
            null -> userEntity
            else -> null
        }
    }

    @Transactional(readOnly = true)
    override fun findValidUserByEmail(email: String): User? {
        val user = findUserByEmail(email)

        return when (user?.deletedAt) {
            null -> user
            else -> null
        }
    }

    // @Transactional(readOnly = true)
    private fun findUserByEmail(email: String): User? = userRepository.findUserEntityByEmail(email)?.toUser()

    @Transactional(readOnly = true)
    override fun findValidUserByEmailAndOauthType(email: String, oAuthChannelType: OAuthChannelType): User? {
        val user = findUserByEmailAndOauthChannelType(email, oAuthChannelType)

        return when (user?.deletedAt) {
            null -> user
            else -> null
        }
    }

    @Transactional(readOnly = true)
    override fun findValidUserById(userId: Long): User? {
        val user = findUserById(userId)

        return when (user?.deletedAt) {
            null -> user
            else -> null
        }
    }

    private fun findUserById(userId: Long): User? = userRepository.findUserEntityById(userId)?.toUser()

    private fun findUserByEmailAndOauthChannelType(email: String,
                                                   oAuthChannelType: OAuthChannelType): User? =
        userRepository.findUserEntityByEmailAndOAuthChannelType(email, oAuthChannelType)?.toUser()


    @Transactional
    override fun updateLastLoginTime(user: User) {
        userRepository.findByIdOrNull(user.id)?.updateLastLoginAt()
    }

    @Transactional
    override fun deleteUserByEmail(email: String): Boolean {
        val userEntity = userRepository.findUserEntityByEmail(email) ?: throw ServiceException(ErrorCode.USER_NOT_FOUND)
        userEntity.deletedAt = LocalDateTime.now()
        return true
    }
}
