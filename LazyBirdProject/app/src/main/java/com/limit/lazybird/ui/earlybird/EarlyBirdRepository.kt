package com.limit.lazybird.ui.earlybird

import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.data.PreferenceDataStoreManager
import javax.inject.Inject

/************* EarlyBirdRepository ***************
 * 메인화면(얼리버드 탭) (Repository)
 * 얼리버드 정보 리스트로 보기
 ********************************************** ***/
class EarlyBirdRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
) {
    fun getPreferenceFlow() = dataStoreManager.preferenceTokenFlow
    suspend fun getEarlyList(token: String) = apiHelper.getEarlyList(token)
}