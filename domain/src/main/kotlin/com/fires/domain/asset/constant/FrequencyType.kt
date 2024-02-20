package com.fires.domain.asset.constant

enum class FrequencyType(
    val value: Int
) {
    NONE(-1),
    ONE_TIME(0),
    ANNUALLY(1),
    BI_ANNUALLY(2),
    QUARTERLY(4),
    MONTHLY(12);

    companion object {
        fun getFrequencyType(number: Int): FrequencyType =
            when (number) {
                0 -> FrequencyType.ONE_TIME
                1 -> FrequencyType.ANNUALLY
                2 -> FrequencyType.BI_ANNUALLY
                4 -> FrequencyType.QUARTERLY
                12 -> FrequencyType.MONTHLY
                else -> FrequencyType.NONE
            }
    }
}
