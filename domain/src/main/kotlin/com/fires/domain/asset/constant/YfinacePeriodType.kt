package com.fires.domain.asset.constant

import java.time.LocalDate
import java.time.temporal.ChronoUnit

enum class YfinacePeriodType(val periodString: String) {
    ONE_DAY("1d"),
    FIVE_DAY("5d"),
    ONE_MONTH("1mon");

    companion object{
        fun from(targetDate: LocalDate): YfinacePeriodType {
            val today = LocalDate.now()
            if(targetDate >= today) return ONE_DAY

            val daysUntilTarget = ChronoUnit.DAYS.between(today, targetDate)
            return when {
                (daysUntilTarget <= 5) -> FIVE_DAY
                else -> ONE_MONTH
            }
        }
    }
}