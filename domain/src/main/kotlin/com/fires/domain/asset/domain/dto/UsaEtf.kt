package com.fires.domain.asset.domain.dto

import com.fires.common.annotation.CsvDto
import com.opencsv.bean.CsvBindByName
/**
 * CSV -> BEAN으로 전환하기 위한 dto
 * OpenCsv가 record를 지원하지 않음
 * @See <a href="https://www.nasdaq.com/market-activity/etf/screener">미국 etf 데이터소스</a>
 */
@CsvDto
data class UsaEtf(
    @CsvBindByName(column = "SYMBOL")
    val tickerCode: String,

    @CsvBindByName(column = "NAME")
    val name: String
)
