package com.limit.lazybird.ui.calendaradd

import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.data.PreferenceDataStoreManager
import javax.inject.Inject

class CalendarAddRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
) {
    fun getPreferenceFlow() = dataStoreManager.preferenceTokenFlow

    suspend fun saveCalendarInfo(
        token:String,
        exhbt_cd: String,
        reser_dt: String,
        start_time: String,
        end_time: String
    ) = apiHelper.saveCalendarInfo(token, exhbt_cd, reser_dt, start_time, end_time)

    suspend fun deleteCalendarInfo(
        token:String,
        exhbt_cd: String
    ) = apiHelper.deleteCalendarInfo(token, exhbt_cd)

    suspend fun saveCustomInfo(
        token:String,
        exhbt_nm: String,
        exhbt_lct: String,
        reser_dt: String,
        start_time: String,
        end_time: String
    ) = apiHelper.saveCustomInfo(token, exhbt_nm, exhbt_lct, reser_dt, start_time, end_time)
}