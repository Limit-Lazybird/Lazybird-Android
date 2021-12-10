package com.limit.lazybird.ui.earlycard

import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.data.PreferenceDataStoreManager
import javax.inject.Inject

/************* EarlycardFragment ***************
 * 메인화면(?? 탭) >> 얼리카드 화면 (Repository)
 * 얼리카드 정보 리스트로 보기
 ********************************************** ***/
class EarlycardRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
) {
    fun getPreferenceTokenFlow() = dataStoreManager.preferenceTokenFlow
}