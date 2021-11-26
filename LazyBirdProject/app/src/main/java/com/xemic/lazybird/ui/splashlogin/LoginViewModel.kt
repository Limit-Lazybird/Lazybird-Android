package com.xemic.lazybird.ui.splashlogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xemic.lazybird.api.ApiHelper
import com.xemic.lazybird.data.PreferenceDataStoreManager
import com.xemic.lazybird.models.retrofit.LoginResponseBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiHelper: ApiHelper,
    private val preferenceDataStoreManager: PreferenceDataStoreManager
): ViewModel() {

    private val loginEventChannel = Channel<LoginEvent>()
    val loginEvent = loginEventChannel.receiveAsFlow()

    fun loginKakao(
        email: String,
        kakaoToken: String,
        name: String
    ) = viewModelScope.launch {
        updateUserInfo(email, name)
        val response = apiHelper.loginKakao(
            email = email,
            token = kakaoToken,
            name = name
        )
        if(response.isSuccessful) {
            loginEventChannel.send(LoginEvent.SuccessLogin(response.body()!!))
        } else {
            loginEventChannel.send(LoginEvent.ErrorOccured(response.errorBody()!!))
        }
    }

    fun loginGoogle(
        email: String,
        googleToken: String,
        name: String
    ) = viewModelScope.launch {
        updateUserInfo(email, name)
        val response = apiHelper.loginGoogle(
            email = email,
            token = googleToken,
            name = name
        )
        if(response.isSuccessful) {
            loginEventChannel.send(LoginEvent.SuccessLogin(response.body()!!))
        } else {
            loginEventChannel.send(LoginEvent.ErrorOccured(response.errorBody()!!))
        }
    }

    fun updateToken(token:String) = viewModelScope.launch {
        preferenceDataStoreManager.updateToken(token)
    }

    fun updateUserInfo(email: String, name: String) = viewModelScope.launch {
        preferenceDataStoreManager.updateUserInfo(email, name)
    }

    sealed class LoginEvent {
        data class SuccessLogin(
            val responseBody: LoginResponseBody
        ) : LoginEvent()

        class ErrorOccured(
            val errorBody: ResponseBody
        ) : LoginEvent()
    }
}