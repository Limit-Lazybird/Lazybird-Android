package com.limit.lazybird.ui.calendar

import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.data.PreferenceDataStoreManager
import javax.inject.Inject

/**************** CalendarRepository ******************
 * 메인화면(캘린더 탭) (Repository)
 * 캘린더에서 (예약된 or 예약되지 않은)전시일정정보 확인
 * Todo : 서버에서 API 받아오기
 ********************************************** ***/
class CalendarRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
) {
    fun getPreferenceFlow() = dataStoreManager.preferenceTokenFlow
    suspend fun getUnRegistList(token: String) = apiHelper.getUnRegistList(token)
    suspend fun getRegistList(token: String) = apiHelper.getRegistListAll(token)
    suspend fun getCustomList(token: String) = apiHelper.getCustomListAll(token)
    suspend fun visitUpdateExhbt(
        token: String,
        exhbt_cd: String,
        visit_yn: String
    ) = apiHelper.visitUpdateExhbt(token, exhbt_cd, visit_yn)
    suspend fun visitUpdateCustom(
        token: String,
        exhbt_cd: String,
        visit_yn: String
    ) = apiHelper.visitUpdateCustom(token, exhbt_cd, visit_yn)
}
