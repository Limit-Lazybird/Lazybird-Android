package com.xemic.lazybird.ui.notification

import com.xemic.lazybird.api.ApiHelper
import com.xemic.lazybird.data.PreferenceDataStoreManager
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