package com.limit.lazybird.repository

import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.datastore.PreferenceDataStoreManager
import javax.inject.Inject

/**************** CalendarRepository ******************
 * 메인화면(캘린더 탭) (Repository)
 * 캘린더에서 (예약된 or 예약되지 않은)전시일정정보 확인
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

    suspend fun deleteCalendarInfo(
        token:String,
        exhbt_cd: String
    ) = apiHelper.deleteCalendarInfo(token, exhbt_cd)

    suspend fun deleteCustomCalendarInfo(
        token:String,
        exhbt_cd: String
    ) = apiHelper.delCustomInfo(token, exhbt_cd)

    suspend fun saveCalendarInfo(
        token:String,
        exhbt_cd: String,
        reser_dt: String,
        start_time: String,
        end_time: String
    ) = apiHelper.saveCalendarInfo(token, exhbt_cd, reser_dt, start_time, end_time)

    suspend fun updateCalendarInfo(
        token:String,
        exhbt_cd: String,
        reser_dt: String,
        start_time: String,
        end_time: String
    ) = apiHelper.saveCalendarInfo(token, exhbt_cd, reser_dt, start_time, end_time)

    suspend fun saveCustomInfo(
        token:String,
        exhbt_nm: String,
        exhbt_lct: String,
        reser_dt: String,
        start_time: String,
        end_time: String
    ) = apiHelper.saveCustomInfo(token, exhbt_nm, exhbt_lct, reser_dt, start_time, end_time)

    suspend fun updateCustomInfo(
        token:String,
        exhbt_cd: String,
        exhbt_nm: String,
        exhbt_lct: String,
        reser_dt: String,
        start_time: String,
        end_time: String
    ) = apiHelper.updateCustomInfo(token, exhbt_cd, exhbt_nm, exhbt_lct, reser_dt, start_time, end_time)
}
