package com.limit.lazybird.repository

import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.datastore.PreferenceDataStoreManager
import javax.inject.Inject


/************* NotificationRepository ***************
 * ?? >> 알림화면 (Repository)
 * 나에게 온 알림 보기
 ********************************************** ***/
class NotificationRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
){
    fun getPreferenceTokenFlow() = dataStoreManager.preferenceTokenFlow
}