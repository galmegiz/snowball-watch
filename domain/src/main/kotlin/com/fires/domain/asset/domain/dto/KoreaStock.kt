package com.fires.domain.asset.domain.dto

import com.fires.common.annotation.CsvDto
import com.opencsv.bean.CsvBindByPosition

/**
 * CSV -> BEAN으로 전환하기 위한 dto
 * OpenCsv가 record를 지원하지 않음
 * @See <a href="http://data.krx.co.kr/contents/MDC/MDI/mdiLoader/index.cmd?menuId=MDC0201020101">krx 주식 - 종목정보 - 전종목 기본정보</a>
 */
@CsvDto
data class KoreaStock(
    @CsvBindByPosition(position = 0) // @CsvBindByName(column = "표준코드")
    var standardCode: String,

    /**
     * 단축코드는 4자리로 구성되며 주권은 0 ~4로 시작, ETN은 5~8로 시작
     */
    @CsvBindByPosition(position = 1) // @CsvBindByName(column = "단축코드")
    val stockCode: String,

    @CsvBindByPosition(position = 2) // @CsvBindByName(column = "한글종목명")
    val name: String,

    @CsvBindByPosition(position = 3) // @CsvBindByName(column = "한글종목약명")
    val shortName: String,

    @CsvBindByPosition(position = 4) // @CsvBindByName(column = "영문종목명")
    val englishName: String,

    @CsvBindByPosition(position = 6) // @CsvBindByName(column = "시장구분") ex) KOSDAQ, KOSPI
    val marketType: String,

    @CsvBindByPosition(position = 9) // @CsvBindByName(column = "주식종류") ex) 보통주, 우선주 등
    val stockType: String
)
