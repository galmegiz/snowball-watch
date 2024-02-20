package com.fires.domain.asset.adapter.event

import com.fires.domain.asset.application.port.out.AssetPricePort
import com.fires.domain.asset.constant.AssetMarketType
import org.springframework.context.ApplicationEvent

class MarketSearchEvent(
    source: AssetPricePort,
    val assetId: Long,
    val marketType: AssetMarketType
) : ApplicationEvent(source)
