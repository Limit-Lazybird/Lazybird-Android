package com.limit.lazybird.repository

import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.models.retrofit.NoticeListResponseBody
import retrofit2.Response
import javax.inject.Inject

/************* NoticeRepository ***************
 * 공지사항 정보를 담당하는 Repository
 ********************************************** ***/
class NoticeRepository @Inject constructor(
    private val apiHelper: ApiHelper
) {
    suspend fun getNoticeList():Response<NoticeListResponseBody> = apiHelper.getNoticeList()
}