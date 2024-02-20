package com.fires.domain.exchangerate.application.port.`in`

import com.fires.domain.exchangerate.domain.dto.ExchangeRate

interface ExchangeRateUseCase {
    fun getExchangeRate(): ExchangeRate
}
