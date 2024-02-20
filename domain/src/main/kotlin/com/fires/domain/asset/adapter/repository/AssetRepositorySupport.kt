package com.fires.domain.asset.adapter.repository

import com.fires.domain.asset.application.port.`in`.AssetQueryCommand
import com.linecorp.kotlinjdsl.QueryFactory
import org.springframework.stereotype.Repository

@Repository
class AssetRepositorySupport(
    private val kdslQueryFactory: QueryFactory
) {
    // ToDo SpringBoot3에 맞에 kotlin-jdls 사용법 확인 필요
    fun findCircleVideoInfoList(
        command: AssetQueryCommand
    ): String {
        return ""
    }
}
