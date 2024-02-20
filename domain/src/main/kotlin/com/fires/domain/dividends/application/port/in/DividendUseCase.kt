package com.fires.domain.dividends.application.port.`in`

import com.fires.domain.dividends.domain.dto.AnnualDividendResponse
import com.fires.domain.dividends.domain.dto.ThisMonthDividendResponse

interface DividendUseCase {
    fun getDividendCalendar(userId: Long): List<ThisMonthDividendResponse>
    fun getAnnualDividendInfo(userId: Long): AnnualDividendResponse
}
