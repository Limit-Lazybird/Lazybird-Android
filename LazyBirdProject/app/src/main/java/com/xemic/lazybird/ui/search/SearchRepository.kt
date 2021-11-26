package com.xemic.lazybird.ui.search

import com.xemic.lazybird.api.ApiHelper
import com.xemic.lazybird.data.PreferenceDataStoreManager
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
) {
    fun getPreferenceFlow() = dataStoreManager.preferenceTokenFlow
    suspend fun searchExhibitionList(
        token: String,
        words: String
    ) = apiHelper.searchExhbtList(token, words)


    // Todo: for test (deleted some day)
    suspend fun getExhibitionList(
        token: String
    ) = apiHelper.getExhbtList(token)
}