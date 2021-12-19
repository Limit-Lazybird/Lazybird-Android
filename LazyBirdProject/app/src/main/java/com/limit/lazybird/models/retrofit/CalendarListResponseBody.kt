package com.limit.lazybird.models.retrofit

data class CalendarListResponseBody(
    val calList: List<CalendarInfo>,
    val code: Int,
    val msg: String
)
