package com.limit.lazybird.viewmodel

import androidx.lifecycle.*
import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.datastore.PreferenceDataStoreManager
import com.limit.lazybird.models.UserInfo
import com.limit.lazybird.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/************* SettingViewModel ***************
 * 메인화면(마이버드 탭) >> 옵션  (ViewModel)
 * 옵션 화면
 ********************************************** ***/
@HiltViewModel
class SettingViewModel @Inject constructor(
    private val repository: UserRepository
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
        token = repository.getPreferenceTokenFlow().first()
    }
    private fun initUserInfo() = viewModelScope.launch {
        _userInfo.postValue(
            repository.getPreferenceUserInfoFlow().first()
        )
    }

    fun deleteUser() = viewModelScope.launch {
        repository.deleteUser(token)
    }
}