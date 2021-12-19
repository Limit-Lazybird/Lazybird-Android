package com.limit.lazybird.repository

import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.models.retrofit.NoticeListResponseBody
import retrofit2.Response
import javax.inject.Inject

class NoticeRepository @Inject constructor(
    private val apiHelper: ApiHelper
) {
    suspend fun getNoticeList():Response<NoticeListResponseBody> = apiHelper.getNoticeList()
}