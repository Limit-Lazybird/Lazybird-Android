package com.limit.lazybird.ui.setting

import androidx.lifecycle.*
import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.data.PreferenceDataStoreManager
import com.limit.lazybird.models.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val apiHelper: ApiHelper,
    private val preferenceDataStoreManager: PreferenceDataStoreManager
):ViewModel() {

    lateinit var token: String

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo get() = _userInfo.asFlow()


    init {
        initToken()
        initUserInfo()
    }

    private fun initToken() = viewModelScope.launch {
        // dataStore 에서 토큰 값 가져오기
        token = preferenceDataStoreManager.preferenceTokenFlow.first()
    }
    private fun initUserInfo() = viewModelScope.launch {
        _userInfo.postValue(
            preferenceDataStoreManager.preferenceUserInfoFlow.first()
        )
    }

    fun deleteUser() = viewModelScope.launch {
        apiHelper.deleteUser(token)
    }
}