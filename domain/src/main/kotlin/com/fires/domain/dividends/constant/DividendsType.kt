package com.fires.domain.dividends.constant

/**
 * 배당 타입
 */
enum class DividendsType(
    val value: String
) {
    REGULAR("CD"), // 일관된 일정에 따라 지급 되었거나 지급될 것으로 예상되는 배당금
    SPECIAL("SC"), // 지급 빈도가 낮거나 비주기적으로 배당된 특별 현금 배당금
    NONE("");

    companion object {
        fun getValue(value: String?) = when (entries.map { it.name }.contains(value)) {
            false -> NONE
            else -> entries.first { it.name == value }
        }
    }
}
