package com.fires.domain.user.adapter.entity

import com.fires.domain.user.constant.OAuthChannelType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "provider_ids")
class ProviderIdEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "provider_id_id", nullable = false)
    val id: Long? = null,
    @Enumerated(EnumType.STRING)
    val oAuthChannelType: OAuthChannelType,
    @OneToOne(fetch = FetchType.LAZY, optional = false, orphanRemoval = true)
    val userEntity: UserEntity
) {

}