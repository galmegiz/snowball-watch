package com.fires.domain.asset.constant

/**
 * Polygon api에서 얻어온 배당타입
 * @See <a href="https://polygon.io/docs/stocks/get_v3_reference_dividends">관련 api 문서</a>
 */
enum class DividendType {
    CD, // Dividends that have been paid and/or are expected to be paid on consistent schedules are denoted as CD
    SC, // Special Cash dividends that have been paid that are infrequent or unusual, and/or can not be expected to occur in the future are denoted as SC
    LT,
    ST // Long-Term and Short-Term capital gain distributions are denoted as LT and ST, respectively.
}
