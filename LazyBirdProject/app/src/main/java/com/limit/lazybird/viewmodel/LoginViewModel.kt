package com.limit.lazybird.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.datastore.PreferenceDataStoreManager
import com.limit.lazybird.models.retrofit.LoginResponseBody
import com.limit.lazybird.repository.UserRepository
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
    private val repository: UserRepository
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
        val response = repository.loginKakao(
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
        val response = repository.loginGoogle(
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
        repository.updateToken(token)
    }

    fun updateUserInfo(loginType: String, email: String, name: String) = viewModelScope.launch {
        // dataStore 에서 유저정보 값 저장하기
        repository.updateUserInfo(loginType, email, name)
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