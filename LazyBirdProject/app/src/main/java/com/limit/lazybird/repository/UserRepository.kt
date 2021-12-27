package com.limit.lazybird.repository

import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.datastore.PreferenceDataStoreManager
import javax.inject.Inject

/************* ExhibitionRepository ***************
 * 전시 관련 정보를 담당하는 Repository
 ********************************************** ***/
class UserRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
) {
    fun getPreferenceTokenFlow() = dataStoreManager.preferenceTokenFlow
    fun getPreferenceUserInfoFlow() = dataStoreManager.preferenceUserInfoFlow

    suspend fun updateToken(token:String) = dataStoreManager.updateToken(token)
    suspend fun updateUserInfo(loginType: String, email: String, name: String) = dataStoreManager.updateUserInfo(loginType, email, name)
    suspend fun loginKakao(token: String) = apiHelper.loginKakao(token)
    suspend fun loginGoogle(token: String) = apiHelper.loginGoogle(token)
    suspend fun deleteUser(token: String) = apiHelper.deleteUser(token)
}