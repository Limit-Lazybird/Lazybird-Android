package com.limit.lazybird.repository

import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.datastore.PreferenceDataStoreManager
import javax.inject.Inject

/************* EarlyCardFragment ***************
 * 얼리카드 관련 정보를 담당하는 Repository
 ********************************************** ***/
class EarlycardRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
) {
    fun getPreferenceTokenFlow() = dataStoreManager.preferenceTokenFlow

    suspend fun getEarlyCardList(token: String) = apiHelper.getEarlyCardList(token)
    suspend fun exhbtReservationSave(token: String, id: String) = apiHelper.exhbtReservationSave(token, id)
}