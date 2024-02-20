package com.fires.common.util

import java.math.BigDecimal
import java.math.RoundingMode

object BigDecimalUtil {
    val ONE_HUNDRED = BigDecimal("100")

    // 백분율 변화를 계산하는 함수
    fun calculatePercentChange(op1: BigDecimal, op2: BigDecimal): BigDecimal {
        if (op2.compareTo(BigDecimal.ZERO) == 0)  {
            return BigDecimal.ZERO
        }
        val percentageChange = op1.divide(op2, 4, RoundingMode.HALF_UP).multiply(BigDecimal("100"))
        return percentageChange.setScale(2, RoundingMode.HALF_UP)
    }

    fun divideRoundToTwoDecimal(op1: BigDecimal, op2: BigDecimal): BigDecimal {
        if (op2.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO
        }
        return op1.divide(op2, 4, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP)
    }
}