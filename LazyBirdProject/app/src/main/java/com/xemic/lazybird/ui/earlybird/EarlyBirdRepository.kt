package com.xemic.lazybird.ui.earlybird

import com.xemic.lazybird.api.ApiHelper
import com.xemic.lazybird.data.PreferenceDataStoreManager
import javax.inject.Inject

class EarlyBirdRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
) {
    suspend fun getTodos() = apiHelper.getTodos()
    fun getPreferenceFlow() = dataStoreManager.preferenceTokenFlow
    suspend fun getEarlyList(token: String) = apiHelper.getEarlyList(token)
}