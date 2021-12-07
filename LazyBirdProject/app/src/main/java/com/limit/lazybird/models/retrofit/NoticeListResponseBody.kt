package com.limit.lazybird.models.retrofit

data class NoticeListResponseBody(
    val noticeList: List<Notice>,
    val code: Int,
    val msg: String
)
