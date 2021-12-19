package com.limit.lazybird.api

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.limit.lazybird.BuildConfig
import com.limit.lazybird.models.LoginInfo

/************** GoogleLoginHelper *****************
 * 반복되는 Google Login 작업을 정리하기 위한 Google Login 전용 객체
 ********************************************** ***/
class GoogleLoginHelper(
    private val fragment: Fragment
) {
    companion object {
        const val TAG = "GoogleLoginHelper"
    }

    private lateinit var gso: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var registerResult: ActivityResultLauncher<Intent>

    // 사용자 로그인정보
    private val _loginInfo = MutableLiveData<LoginInfo>()
    val loginInfo : LiveData<LoginInfo> get() =  _loginInfo

    // GoogleLogin 을 위한 초기화
    fun init() {
        Log.e(TAG, "init() called")
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(fragment.context, gso)
        registerResult =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                Log.e(TAG, "${result}")
                Log.e(TAG, "${result.data?.extras}")
                if (result.resultCode == Activity.RESULT_OK) {
                    // Result OK 일 때, SignIn 한 결과에서 account 가져오기
                    val data = result.data
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        // success
                        Log.w(TAG, "login success")
                        val account = task.getResult(ApiException::class.java)
                        updateUserInfo(account)
                    } catch (e: ApiException){
                        // fail
                        Log.w(TAG, "signInResult:failed code= "+ e.statusCode)
                    }
                }
            }
    }

    // 로그인
    fun login() {
        if(!this::gso.isInitialized)
            init()

        Log.e(TAG, "login() called")
        var account = GoogleSignIn.getLastSignedInAccount(fragment.context)
        if (account == null || account.isExpired) {
            Log.e(TAG, "account is null or token is Expired")
            val intent = mGoogleSignInClient.signInIntent
            Log.e(TAG, "${intent}")
            registerResult.launch(intent)
        } else {
            Log.e(TAG, "account is exists")
            updateUserInfo(account) // 기존 사용자
        }
    }

    // 로그아웃
    fun logout() {
        if(!this::gso.isInitialized)
            init()

        mGoogleSignInClient.signOut()
            .addOnCanceledListener {
                Log.e(TAG, "로그인이 완료되었습니다.")
            }
    }

    // 회원탈퇴
    fun memberOut() {
        if(!this::gso.isInitialized)
            init()

        mGoogleSignInClient.revokeAccess()
            .addOnCanceledListener {
                Log.e(TAG, "로그인이 완료되었습니다.")
            }
    }

    // 구글 로그인 성공
    fun updateUserInfo(account: GoogleSignInAccount){
        /*** kakao API response ***
         * accountName : account.account.name
         * accountType : account.account.type
         * displayName : account.displayName
         * email : account.email
         * givenName : account.givenName
         * id : account.id
         * photoUrl : account.photoUrl
         * serverAuthToken : account.serverAuthCode
         * idToken : account.idToken
         * isExpired : account.isExpired
         * *** ***************** ***/
        Log.e(TAG, "update success: ${account.email} ${account.displayName} ${account.serverAuthCode} ${account.idToken}")
        _loginInfo.postValue(
            LoginInfo(
                "google",
                account.email,
                account.displayName,
                account.idToken
            )
        )
    }
}