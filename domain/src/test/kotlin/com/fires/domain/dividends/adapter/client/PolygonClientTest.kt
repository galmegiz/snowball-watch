package com.fires.domain.dividends.adapter.client

import com.fires.domain.BaseTest
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.YearMonth

@SpringBootTest
class PolygonClientTest(
    @Autowired private val polygonClient: PolygonClient
) : BaseTest() {

    @Test
    fun `주식 조회 성공 테스트`() = runTest {
        val result = polygonClient.readDividends("MSFT", YearMonth.of(2021, 1))

        result.size shouldNotBe 0
    }
}
