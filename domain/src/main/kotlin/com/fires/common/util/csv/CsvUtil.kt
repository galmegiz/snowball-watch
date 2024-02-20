package com.fires.common.util.csv

import com.fires.common.logging.Log
import com.opencsv.CSVReader
import com.opencsv.bean.CsvToBeanBuilder
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset

@Component
class CsvUtil : Log {
    /**
     * OpenCsv라이브러리를 이용해 csv파일을 객체로 변환해준다.
     * @param fileName db에 저장하고자 하는 정보가 포함된 csv 파일명
     * @param clazz 변환 할 클래스
     * @return
     * @param <T> 변환 받을 클래스
     </T> */
    fun <T> csvToObject(fileName: String, clazz: Class<T>?): List<T> {
        try {
            ClassPathResource(fileName).inputStream.use {
                CSVReader(InputStreamReader(it, Charset.forName("EUC-KR")))
                    .use { reader ->
                        return CsvToBeanBuilder<T>(reader)
                            .withType(clazz)
                            .build()
                            .parse()
                    }
            }
        } catch (e: IOException) {
            log.error("csv read error", e)
        }
        return emptyList()
    }
}
