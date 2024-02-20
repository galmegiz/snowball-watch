package com.fires.domain.portfolio.domain.entity

import com.fires.common.exception.ErrorCode
import com.fires.common.exception.ServiceException

data class Portfolio(
    val id: Long,
    val userId: Long,
    val priority: Long,
    val assetList: List<SimplePortfolioAsset>
){
    fun withValidUserCheck(userId: Long): Portfolio {
        if(this.userId != userId) throw ServiceException(ErrorCode.PORTFOLIO_INVALID_USER)
        return this
    }
}
