package com.xemic.lazybird.ui.exhibition

import com.xemic.lazybird.api.ApiHelper
import com.xemic.lazybird.data.PreferenceDataStoreManager
import javax.inject.Inject

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