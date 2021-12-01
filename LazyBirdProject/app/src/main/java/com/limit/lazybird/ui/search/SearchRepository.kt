package com.limit.lazybird.ui.search

import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.data.PreferenceDataStoreManager
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