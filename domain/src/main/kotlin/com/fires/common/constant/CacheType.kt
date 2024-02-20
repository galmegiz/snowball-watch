package com.fires.common.constant

enum class CacheType(
    val cacheName: String,
    val capacity: Int,
    val maximumSize: Long,
    val expireTime: Long
) {
    DEFAULT(CacheConstant.DEFAULT, 100, 500L, 60L),
    USER(CacheConstant.USER, 100, 1000L, 1L),
    LOGOUT_USER(CacheConstant.LOGOUT_USER_TOKEN, 100, 1000L, 86400L), // 1일 (60 * 60 * 24)
    KIS(CacheConstant.KIS, 1, 1, 86400L), // 1일 (60 * 60 * 24)
    ECOS(CacheConstant.ECOS, 1, 1, 3600L), // 1시간 (60 * 60)
    // PRICE_WITH_CHANGE(CacheConstant.PRICE_WITH_CHANGE, 200, 200L, 300L), // 5분 ( 60 * 5)
    // PRICE_WITHOUT_CHANGE(CacheConstant.PRICE_WITHOUT_CHANGE, 200, 200L, 300L), // 5분 ( 60 * 5)
    DIVIDEND(CacheConstant.DIVIDEND, 200, 200L, 300L), // 5분 ( 60 * 5)
    PRICE(CacheConstant.PRICE, 200, 200L, 300L), // 5분 ( 60 * 5)
    // DIVIDEND_THIS_MONTH(CacheConstant.DIVIDEND_THIS_MONTH, 200, 200L, 300L), // 5분 ( 60 * 5)
}
