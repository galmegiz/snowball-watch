package com.fires.domain.exchangerate.application.port.out

import com.fires.domain.exchangerate.domain.dto.ExchangeRate

interface ExchangeRatePort {
    fun getUsdExchangeRate(): ExchangeRate
}
