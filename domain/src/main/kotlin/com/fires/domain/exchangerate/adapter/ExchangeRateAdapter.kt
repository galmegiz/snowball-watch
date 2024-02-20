package com.fires.domain.exchangerate.adapter

import com.fires.common.constant.CurrencyType
import com.fires.domain.exchangerate.adapter.client.EcosClient
import com.fires.domain.exchangerate.application.port.out.ExchangeRatePort
import com.fires.domain.exchangerate.domain.dto.ExchangeRate
import org.springframework.stereotype.Component

@Component
class ExchangeRateAdapter(
    private val ecosClient: EcosClient
) : ExchangeRatePort {
    override fun getUsdExchangeRate(): ExchangeRate {
        val result = ecosClient.getUsdExchangeRate()
        return ExchangeRate(
            value = result.value.toDouble(),
            currencyType = CurrencyType.USD
        )
    }
}
