package com.fires.common.auth.adapter

import com.fires.common.auth.adapter.entity.UserRefreshTokenEntity
import com.fires.common.auth.adapter.repository.UserRefreshTokenRepository
import com.fires.common.auth.application.port.out.UserRefreshTokenCreateCommand
import com.fires.common.auth.application.port.out.UserRefreshTokenPort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserRefreshTokenAdapter(
    private val userRefreshTokenRepository: UserRefreshTokenRepository
) : UserRefreshTokenPort {
    @Transactional
    override fun save(request: UserRefreshTokenCreateCommand) {
        val userRefreshTokenEntity = UserRefreshTokenEntity(
            key = request.key,
            value = request.value
        )
        userRefreshTokenRepository.save(userRefreshTokenEntity)
    }

    @Transactional(readOnly = true)
    override fun findByKey(id: String): UserRefreshTokenEntity? = userRefreshTokenRepository.findByKey(id)

    @Transactional
    override fun updateToken(entity: UserRefreshTokenEntity, token: String) {
        entity.updateValue(token)
    }
}
