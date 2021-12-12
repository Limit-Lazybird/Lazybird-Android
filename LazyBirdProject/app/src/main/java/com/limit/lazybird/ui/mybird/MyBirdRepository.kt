package com.limit.lazybird.ui.mybird

import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.data.PreferenceDataStoreManager
import javax.inject.Inject

/************* MyBirdRepository ***************
 * 메인화면(마이버드 탭) (Repository)
 * 마이버드 화면 (내 정보 보기)
 ********************************************** ***/
class MyBirdRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
) {
    fun getPreferenceTokenFlow() = dataStoreManager.preferenceTokenFlow
    fun getPreferenceUserInfoFlow() = dataStoreManager.preferenceUserInfoFlow
    suspend fun getLikeExhibitionList(token:String) = apiHelper.getExhbtLikeList(token)
    suspend fun getReservationExhibitionList(token:String) = apiHelper.getExhbtReservationList(token)
    suspend fun deleteReservationExhibition(token: String, exhbt_cd: String) = apiHelper.exhbtReservationDelete(token, exhbt_cd)
}