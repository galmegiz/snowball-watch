package com.fires.domain.dividends.domain.dto

data class PolygonPrevCloseResponse(
    val ticker: String,
    val resultCount: Int,
    val results: List<PolygonPrevClose>
)
