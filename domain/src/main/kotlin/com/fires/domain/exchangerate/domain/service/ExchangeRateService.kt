package com.fires.domain.exchangerate.domain.service

import com.fires.common.logging.Log
import com.fires.domain.exchangerate.application.port.`in`.ExchangeRateUseCase
import com.fires.domain.exchangerate.application.port.out.ExchangeRatePort
import com.fires.domain.exchangerate.domain.dto.ExchangeRate
import org.springframework.stereotype.Service

@Service
class ExchangeRateService(
    private val exchangeRatePort: ExchangeRatePort
) : Log, ExchangeRateUseCase {
    override fun getExchangeRate(): ExchangeRate {
        return exchangeRatePort.getUsdExchangeRate()
    }
}
