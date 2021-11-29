package com.limit.lazybird.ui.setting

import androidx.lifecycle.*
import com.limit.lazybird.data.PreferenceDataStoreManager
import com.limit.lazybird.models.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val preferenceDataStoreManager: PreferenceDataStoreManager
):ViewModel() {
    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo get() = _userInfo.asFlow()

    init {
        initUserInfo()
    }

    private fun initUserInfo() = viewModelScope.launch {
        _userInfo.postValue(
            preferenceDataStoreManager.preferenceUserInfoFlow.first()
        )
    }
}