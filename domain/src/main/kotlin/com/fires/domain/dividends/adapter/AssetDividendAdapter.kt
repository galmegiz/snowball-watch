package com.fires.domain.dividends.adapter

import com.fires.domain.asset.domain.entity.Asset
import com.fires.domain.dividends.adapter.entity.AssetDividendEntity
import com.fires.domain.dividends.adapter.repository.AssetDividendRepository
import com.fires.domain.dividends.application.port.out.AssetDividendPort
import com.fires.domain.dividends.domain.dto.DividendSaveCommand
import com.fires.domain.dividends.domain.entity.Dividend
import org.springframework.stereotype.Component
import java.time.YearMonth

@Component
class AssetDividendAdapter(
    private val assetDividendRepository: AssetDividendRepository
): AssetDividendPort {
    override fun saveDividends(dividendSaveCommand: List<DividendSaveCommand>) {
        val entities = dividendSaveCommand.map{command ->
            val dividend = command.dividend
            AssetDividendEntity(
                dividend = dividend.cashAmount.toBigDecimal(),
                payDate = dividend.payDate.takeUnless { it != dividend.exDividendDate },
                exDividendDate = dividend.exDividendDate,
                assetEntity = command.assetEntity
            )
        }
        assetDividendRepository.saveAll(entities)
    }

    override fun queryDividends(asset: Asset, from: YearMonth): List<Dividend> {
        TODO("Not yet implemented")
    }

    override fun queryDividendsThisMonth(asset: Asset, from: YearMonth): List<Dividend> {
        TODO("Not yet implemented")
    }
}