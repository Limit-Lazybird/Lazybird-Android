package com.limit.lazybird.models.retrofit

data class EarlyCardResponseBody(
    val earlyCardList: List<EarlyCard>,
    val code: Int,
    val msg: String
)
