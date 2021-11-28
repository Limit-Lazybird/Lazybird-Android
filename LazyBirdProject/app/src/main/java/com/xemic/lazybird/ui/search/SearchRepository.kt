package com.xemic.lazybird.ui.search

import com.xemic.lazybird.api.ApiHelper
import com.xemic.lazybird.data.PreferenceDataStoreManager
import javax.inject.Inject

/************* SearchRepository ***************
 * 메인화면(검색 탭) (Repository)
 * 검색 화면
 ********************************************** ***/
class SearchRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
) {
    fun getPreferenceFlow() = dataStoreManager.preferenceTokenFlow
    suspend fun searchExhibitionList(
        token: String,
        words: String
    ) = apiHelper.searchExhbtList(token, words)
}