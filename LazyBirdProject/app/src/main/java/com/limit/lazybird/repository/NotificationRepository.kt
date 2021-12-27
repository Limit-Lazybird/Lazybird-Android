package com.limit.lazybird.repository

import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.datastore.PreferenceDataStoreManager
import javax.inject.Inject


/************* NotificationRepository ***************
 * 알림 정보를 담당하는 Repository
 ********************************************** ***/
class NotificationRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
){
    fun getPreferenceTokenFlow() = dataStoreManager.preferenceTokenFlow
}