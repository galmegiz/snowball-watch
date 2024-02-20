package com.fires.domain.asset.domain.dto

import com.fires.common.annotation.CsvDto
import com.opencsv.bean.CsvBindByName
/**
 * CSV -> BEAN으로 전환하기 위한 dto
 * OpenCsv가 record를 지원하지 않음
 * @See <a href="https://www.nasdaq.com/market-activity/stocks/screener">미국 주식 데이터소스</a>
 */
@CsvDto
data class UsaStock(
    @CsvBindByName(column = "Symbol")
    val tickerCode: String,

    @CsvBindByName(column = "Name")
    val name: String,

    @CsvBindByName(column = "Country")
    val country: String,

    @CsvBindByName(column = "Sector")
    val sector: String,

    @CsvBindByName(column = "Industry")
    val industry: String
)
