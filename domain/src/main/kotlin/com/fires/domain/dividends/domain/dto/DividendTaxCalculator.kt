package com.fires.domain.dividends.domain.dto

import com.fires.domain.dividends.domain.entity.Dividend
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

class DividendTaxCalculator {
    private val criteriaDay = LocalDateTime.now().dayOfYear
    var paidTax: BigDecimal = BigDecimal.ZERO
        get(){
            return field.setScale(2, RoundingMode.HALF_UP)
        }
    var unPaidTax: BigDecimal = BigDecimal.ZERO
        get(){
            return field.setScale(2, RoundingMode.HALF_UP)
        }

    fun calculateTax(dividend: Dividend, cashAmountWithExchange: BigDecimal) {
        val dividendDay = dividend.payDate.dayOfYear
        if (dividendDay < criteriaDay) {
            paidTax += cashAmountWithExchange
        } else {
            unPaidTax += cashAmountWithExchange
        }
    }

}