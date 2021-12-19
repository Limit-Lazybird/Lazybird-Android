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
import com.limit.lazybird.R
import com.limit.lazybird.models.LoginInfo

class GoogleLoginHelper(
    private val fragment: Fragment
) {
    companion object {
        const val TAG = "GoogleLoginHelper"
    }

    private lateinit var gso: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var registerResult: ActivityResultLauncher<Intent>

    private val _loginInfo = MutableLiveData<LoginInfo>()
    val loginInfo : LiveData<LoginInfo> get() =  _loginInfo

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

    fun logout() {
        if(!this::gso.isInitialized)
            init()

        mGoogleSignInClient.signOut()
            .addOnCanceledListener {
                Log.e(TAG, "로그인이 완료되었습니다.")
            }
    }

    fun memberOut() {
        if(!this::gso.isInitialized)
            init()

        mGoogleSignInClient.revokeAccess()
            .addOnCanceledListener {
                Log.e(TAG, "로그인이 완료되었습니다.")
            }
    }

    fun updateUserInfo(account: GoogleSignInAccount){
        // 구글 로그인 성공
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