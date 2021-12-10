package com.limit.lazybird.ui.earlycard

import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.data.PreferenceDataStoreManager
import javax.inject.Inject

class EarlycardRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
) {
    fun getPreferenceTokenFlow() = dataStoreManager.preferenceTokenFlow
}