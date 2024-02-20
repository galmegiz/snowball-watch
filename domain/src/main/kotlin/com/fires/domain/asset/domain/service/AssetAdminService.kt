package com.fires.domain.asset.domain.service

import com.fires.common.logging.Log
import com.fires.common.util.csv.CsvUtil
import com.fires.domain.asset.application.port.`in`.AssetAdminUseCase
import com.fires.domain.asset.application.port.out.AssetCreateCommand
import com.fires.domain.asset.application.port.out.AssetPort
import com.fires.domain.asset.application.port.out.CategoryCreateCommand
import com.fires.domain.asset.constant.AssetCategoryType
import com.fires.domain.asset.constant.AssetConstant.KONEX_NAME
import com.fires.domain.asset.constant.AssetConstant.KOREA_ETF_INIT_INFO
import com.fires.domain.asset.constant.AssetConstant.KOREA_STOCK_INIT_INFO
import com.fires.domain.asset.constant.AssetConstant.KOSDAQ_GLOBAL_NAME
import com.fires.domain.asset.constant.AssetConstant.KOSDAQ_NAME
import com.fires.domain.asset.constant.AssetConstant.KOSPI_NAME
import com.fires.domain.asset.constant.AssetConstant.USA_ETF_INIT_INFO
import com.fires.domain.asset.constant.AssetMarketType
import com.fires.domain.asset.constant.CountryType
import com.fires.domain.asset.domain.dto.KoreaEtf
import com.fires.domain.asset.domain.dto.KoreaStock
import com.fires.domain.asset.domain.dto.UsaEtf
import com.fires.domain.asset.domain.dto.UsaStock
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

@Service
class AssetAdminService(
    private val assetPort: AssetPort,
    private val csvUtil: CsvUtil
) : Log, AssetAdminUseCase {
    override fun /**/initAsset() {
        log.info("initAsset")
        // NASDAQ stock
/*        initUsaStockInfo(
            fileName = NASDAQ_STOCK_INIT_INFO,
            marketType = AssetMarketType.NASDAQ
        )
        // NYSE stock
        initUsaStockInfo(
            fileName = NYSE_STOCK_INIT_INFO,
            marketType = AssetMarketType.NYSE
        )
        // AMEX stock
        initUsaStockInfo(
            fileName = AMEX_STOCK_INIT_INFO,
            marketType = AssetMarketType.AMEX
        )*/
/*        // USA ETF
        initUsaEtf()*/
        // Korea stock
        initKoreaStockInfo()
        // Korea ETF
        initKoreaEtfInfo()
    }

    private fun initUsaStockInfo(fileName: String, marketType: AssetMarketType) {
        val usaStockList: List<UsaStock> = csvUtil.csvToObject(fileName, UsaStock::class.java)
        val category = CategoryCreateCommand(CountryType.USA, marketType, AssetCategoryType.STOCK)

        val stockCreateCommandList = usaStockList
            .filter { stock -> StringUtils.hasLength(stock.industry) }
            .map {
                AssetCreateCommand(
                    tickerCode = it.tickerCode,
                    name = it.name,
                    category = category
                )
            }

        assetPort.saveAll(stockCreateCommandList)
    }

    private fun initUsaEtf() {
        val usaEtfList: List<UsaEtf> = csvUtil.csvToObject(USA_ETF_INIT_INFO, UsaEtf::class.java)
        val category = CategoryCreateCommand(countryType = CountryType.USA, assetCategoryType = AssetCategoryType.ETF)

        val createCommandList = usaEtfList
            .map {
                AssetCreateCommand(
                    tickerCode = it.tickerCode,
                    name = it.name,
                    category = category
                )
            }
        assetPort.saveAll(createCommandList)
    }

    private fun initKoreaStockInfo() {
        val koreaStockList: List<KoreaStock> = csvUtil.csvToObject(KOREA_STOCK_INIT_INFO, KoreaStock::class.java)

        val createCommandList = koreaStockList.map {
            val marketType = when (it.marketType) {
                KOSPI_NAME -> AssetMarketType.KRX_KOSPI
                KOSDAQ_NAME, KOSDAQ_GLOBAL_NAME -> AssetMarketType.KRX_KOSDAQ
                KONEX_NAME -> AssetMarketType.KRX_KONEX
                else -> AssetMarketType.UNKNOWN
            }

            val category = CategoryCreateCommand(CountryType.KOR, marketType, AssetCategoryType.STOCK)
            AssetCreateCommand(
                stockCode = it.stockCode,
                name = it.name,
                category = category
            )
        }
        assetPort.saveAll(createCommandList)
    }

    private fun initKoreaEtfInfo() {
        val koreaEtfList: List<KoreaEtf> = csvUtil.csvToObject(KOREA_ETF_INIT_INFO, KoreaEtf::class.java)
        val category = CategoryCreateCommand(
            countryType = CountryType.KOR,
            marketType = AssetMarketType.KRX,
            assetCategoryType = AssetCategoryType.ETF
        )

        val createCommandList = koreaEtfList.map {
            AssetCreateCommand(
                stockCode = it.etfCode,
                name = it.name,
                category = category
            )
        }
        assetPort.saveAll(createCommandList)
    }
}
