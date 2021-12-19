package com.limit.lazybird.api

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.limit.lazybird.BuildConfig
import com.limit.lazybird.R
import com.limit.lazybird.models.LoginInfo
import com.limit.lazybird.ui.splashlogin.LoginFragment

/************** KakaoLoginHelper *****************
 * 반복되는 Kakao Login 작업을 정리하기 위한 Google Login 전용 객체
 ********************************************** ***/
class KakaoLoginHelper(
    private val context: Context
) {
    companion object {
        const val TAG = "KakaoLoginHelper"
    }

    private lateinit var callback: (OAuthToken?, Throwable?) -> Unit

    // 사용자 로그인정보
    private val _loginInfo = MutableLiveData<LoginInfo>()
    val loginInfo: LiveData<LoginInfo> get() =  _loginInfo

    // GoogleLogin 을 위한 초기화
    fun init() {
        Log.e(TAG, "init() called")
        KakaoSdk.init(context, BuildConfig.KAKAO_NATIVE_APP_KEY)
        callback = { token, error ->
            if (error != null) {
                Log.e(LoginFragment.TAG, "로그인 실패 : ${error}")
            } else if (token != null) {
                Log.e(LoginFragment.TAG, "로그인 성공")
                UserApiClient.instance.me { user, error ->
                    when {
                        error != null -> {
                            Log.e(LoginFragment.TAG, "사용자 정보 요청 실패: ${error}")
                        }
                        user != null -> {
                            updateUserInfo(user, token)
                        }
                        else -> {
                            Log.e(LoginFragment.TAG, "user is null")
                        }
                    }
                }
            }
        }
    }

    // 로그인
    fun login(){
        Log.e(TAG, "login() called")
        if(!this::callback.isInitialized)
            init()

        if (AuthApiClient.instance.hasToken()) {
            // 이미 로그인하여 토큰을 발급받았었음
            Log.e(TAG, "token exists")
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    Log.e(TAG, "error!")
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        // 로그인 필요
                        UserApiClient.instance.loginWithKakaoAccount(
                            context,
                            callback = callback
                        )
                    } else {
                        // 기타 에러
                        Log.e("LoginActivity", "error occured")
                        error.printStackTrace()
                    }
                } else {
                    // 토큰 유효성 체크 성공
                    Log.e(TAG, "not error")
                    UserApiClient.instance.me { user, error ->
                        when {
                            error != null -> {
                                Log.e(TAG, "사용자 정보 요청 실패: $error")
                            }
                            user != null -> {
                                // MainActivity 로 이동
                                updateUserInfo(
                                    user,
                                    AuthApiClient.instance.tokenManagerProvider.manager.getToken()!!
                                )
                            }
                            else -> {
                                Log.e(TAG, "user is null")
                            }
                        }
                    }
                }
            }
        } else {
            // 로그인 필요
            Log.e(TAG, "token not exists")
            UserApiClient.instance.loginWithKakaoAccount(
                context,
                callback = callback
            )
        }
    }

    // 로그아웃
    fun logout(){
        if(!this::callback.isInitialized)
            init()

        UserApiClient.instance.logout { error ->
            if(error != null) {
                Log.e(TAG, "로그아웃 실패: ${error}")
            } else {
                Log.e(TAG, "로그아웃 성공")
            }
        }
    }

    // 회원탈퇴
    fun memberOut(){
        if(!this::callback.isInitialized)
            init()

        UserApiClient.instance.unlink { error ->
            if(error != null) {
                Log.e(TAG, "회원탈퇴 실패: ${error}")
            } else {
                Log.e(TAG, "회원탈퇴 성공")
            }
        }
    }

    // 카카오 로그인 성공
    private fun updateUserInfo(user: User, token: OAuthToken){
        /*** kakao API response ***
         * id : user?.id
         * profileNickname : user?.kakaoAccount?.profile?.nickname
         * accountEmail : user?.kakaoAccount?.gender
         * birthday : user?.kakaoAccount?.ageRange
         * accessToken : token.accessToken
         * *** ***************** ***/
        Log.e(TAG, "updateUserInfo() called")
        Log.e(TAG, "${user?.kakaoAccount?.email.toString()}, ${user?.kakaoAccount?.profile?.nickname.toString()}, ${token.accessToken}")

        _loginInfo.postValue(
            LoginInfo(
                "kakao",
                user?.kakaoAccount?.email.toString(),
                user?.kakaoAccount?.profile?.nickname.toString(),
                token.accessToken
            )
        )
    }
}