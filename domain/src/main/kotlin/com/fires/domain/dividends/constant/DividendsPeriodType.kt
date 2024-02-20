package com.fires.domain.dividends.constant

/**
 * 배당주기
 */
enum class DividendsPeriodType(
    private val value: Int
) {
    ONE_TIME(0),
    ANNUALLY(1),
    BI_ANNUALLY(2),
    QUARTERLY(4),
    MONTHLY(12),
    NONE(-1);

    companion object {
        fun getValue(value: Int?) = when (entries.map { it.value }.contains(value)) {
            false -> NONE
            else -> entries.first { it.value == value }
        }
    }
}
