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

/************* LoginViewModel ***************
 * 로그인 화면 (ViewModel)
 * 로그인 화면 (카카오 로그인, 구글 로그인)
 ********************************************** ***/
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
        // LazyBird 서버에 카카오 로그인 요청
        updateUserInfo("kakao", email, name)
        val response = apiHelper.loginKakao(
            token = kakaoToken
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
        // LazyBird 서버에 구글 로그인 요청
        updateUserInfo("google", email, name)
        val response = apiHelper.loginGoogle(
            token = googleToken
        )
        if(response.isSuccessful) {
            loginEventChannel.send(LoginEvent.SuccessLogin(response.body()!!))
        } else {
            loginEventChannel.send(LoginEvent.ErrorOccured(response.errorBody()!!))
        }
    }

    fun updateToken(token:String) = viewModelScope.launch {
        // dataStore 에서 토큰 값 저장하기
        preferenceDataStoreManager.updateToken(token)
    }

    fun updateUserInfo(loginType: String, email: String, name: String) = viewModelScope.launch {
        // dataStore 에서 유저정보 값 저장하기
        preferenceDataStoreManager.updateUserInfo(loginType, email, name)
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