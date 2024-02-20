package com.fires.domain.dividends.application.port.out

import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.dividends.domain.entity.Dividend
import java.time.YearMonth

interface DividendApiPort {
    fun queryDividends(asset: Asset, from: YearMonth): List<Dividend>
    fun queryDividendsThisMonth(asset: Asset, from: YearMonth): List<Dividend>
}
