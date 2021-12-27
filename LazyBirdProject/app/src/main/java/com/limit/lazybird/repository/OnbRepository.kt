package com.limit.lazybird.repository

import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.datastore.PreferenceDataStoreManager
import javax.inject.Inject

/************* OnbRepository ***************
 * 온보딩 관련 정보를 담당하는 Repository
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