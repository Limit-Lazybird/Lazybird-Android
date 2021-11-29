package com.limit.lazybird.ui.onboarding

import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.data.PreferenceDataStoreManager
import javax.inject.Inject

/************* OnbRepository ***************
 * 온보딩 시작화면 >> 온보딩 화면 (Repository)
 * 온보딩 설문조사하는 화면
 ********************************************** ***/
class OnbRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
) {
    fun getPreferenceFlow() = dataStoreManager.preferenceTokenFlow
    suspend fun getCustomizedList(token: String) = apiHelper.getCustomizedList(token)
    suspend fun deleteCustomizedList(token: String) = apiHelper.deleteCustomizedList(token)
    suspend fun insertCustomizedList(
        token: String,
        answer_idx:String
    ) = apiHelper.insertCustomizedList(token, answer_idx)
}