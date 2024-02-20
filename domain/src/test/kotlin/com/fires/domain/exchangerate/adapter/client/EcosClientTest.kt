package com.fires.domain.exchangerate.adapter.client

import com.fires.domain.BaseTest
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class EcosClientTest(
    @Autowired private val ecosClient: EcosClient
) : BaseTest() {

    @Test
    fun `환율 조회 성공 테스트`() = runTest {
        val result = ecosClient.getUsdExchangeRate()

        result shouldNotBe null
    }
}
