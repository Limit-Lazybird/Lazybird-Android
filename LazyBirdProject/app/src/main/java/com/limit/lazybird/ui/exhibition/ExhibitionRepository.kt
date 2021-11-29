package com.limit.lazybird.ui.exhibition

import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.data.PreferenceDataStoreManager
import javax.inject.Inject

/************* ExhibitionRepository ***************
 * 메인화면(전시 탭) (Repository)
 * 전시 정보 전체 보기
 ********************************************** ***/
class ExhibitionRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
) {
    fun getPreferenceFlow() = dataStoreManager.preferenceTokenFlow
    suspend fun getExhibitionList(token:String) = apiHelper.getExhbtList(token)
    suspend fun getCustomExhibitionList(token:String) = apiHelper.getCustomExhbtList(token)
    suspend fun exhbtLikeDel(token: String, exhbt_cd: String) = apiHelper.exhbtLikeDel(token, exhbt_cd)
    suspend fun exhbtLikeSave(token: String, exhbt_cd: String) = apiHelper.exhbtLikeSave(token, exhbt_cd)
    suspend fun filterExhbtList(token: String, searchList:String) = apiHelper.filterDetailExhbtList(token, searchList)
}