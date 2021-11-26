package com.xemic.lazybird.ui.mybird

import com.xemic.lazybird.api.ApiHelper
import com.xemic.lazybird.data.PreferenceDataStoreManager
import javax.inject.Inject

class MyBirdRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
) {
    fun getPreferenceTokenFlow() = dataStoreManager.preferenceTokenFlow
    fun getPreferenceUserInfoFlow() = dataStoreManager.preferenceUserInfoFlow
    suspend fun getLikeExhibitionList(token:String) = apiHelper.getExhbtLikeList(token)
    suspend fun getReservationExhibitionList(token:String) = apiHelper.getExhbtReservationList(token)
}