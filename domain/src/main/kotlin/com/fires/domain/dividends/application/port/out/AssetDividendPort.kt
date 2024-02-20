package com.fires.domain.dividends.application.port.out

import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.dividends.domain.dto.DividendSaveCommand
import com.fires.domain.dividends.domain.entity.Dividend
import java.time.YearMonth

interface AssetDividendPort {
    fun saveDividends(dividendSaveCommand: List<DividendSaveCommand>)
    fun queryDividends(asset: Asset, from: YearMonth): List<Dividend>
    fun queryDividendsThisMonth(asset: Asset, from: YearMonth): List<Dividend>
}