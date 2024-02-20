package com.fires.domain.dividends.constant

object PolygonConstant {
    const val POLYGON_DIVIDENDS_QUERY = "https://api.polygon.io/v3/reference/dividends?ticker=%s&order=asc" +
        "&sort=pay_date&apiKey=%s&pay_date.gte=%s&pay_date.lt=%s&limit=12"
    const val POLYGON_DIVIDENDS_QUERY_THIS_MONTH = "https://api.polygon.io/v3/reference/dividends?ticker=%s&order=desc" +
        "&apiKey=%s&pay_date.gte=%s&pay_date.lte=%s&limit=1&dividend_type=CD"
    const val POLYGON_PREVIOUS_CLOSE_QUERY = "https://api.polygon.io/v2/aggs/ticker/%s/prev?adjusted=true&apiKey=%s"
    const val POLYGON_DAILY_CLOSE_QUERY = "https://api.polygon.io/v1/open-close/%s/%s?adjusted=true&apiKey=%s"
}
