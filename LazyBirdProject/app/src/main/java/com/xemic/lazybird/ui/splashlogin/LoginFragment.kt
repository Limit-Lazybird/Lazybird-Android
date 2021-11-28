package com.xemic.lazybird.ui.splashlogin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.FragmentLoginBinding
import com.xemic.lazybird.ui.MainActivity
import com.xemic.lazybird.ui.MainFragment
import com.xemic.lazybird.ui.onboarding.OnbStartFragment
import com.xemic.lazybird.util.removeAllBackStack
import com.xemic.lazybird.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

/************* LoginFragment ***************
 * 로그인 화면 (Fragment)
 * 로그인 화면 (카카오 로그인, 구글 로그인)
 ********************************************** ***/
@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    companion object {
        const val TAG = "LoginFragment"
    }

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    // for kakao login
    private lateinit var callback: (OAuthToken?, Throwable?) -> Unit

    // for google login
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var registerResult: ActivityResultLauncher<Intent>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        // Kakao Init
        kakaoLoginInit()

        // GoogleSignInClient 개체 구성
        googleLoginInit()


        binding.loginGoogleBtn.setOnClickListener {
            // 구글로 로그인하기 버튼 클릭
            var account = GoogleSignIn.getLastSignedInAccount(context)
            if (account == null) {
                registerResult.launch(mGoogleSignInClient.signInIntent) // 기존 로그인 사용자가 아닌 경우 (회원가입 진행)
            } else {
                successGoogleLogin(account) // 기존 사용자
            }
        }

        binding.loginKakaoBtn.setOnClickListener {
            // 카카오로 로그인하기 버튼 클릭
            if (AuthApiClient.instance.hasToken()) {
                // 이미 로그인하여 토큰을 발급받았었음
                UserApiClient.instance.accessTokenInfo { _, error ->
                    if (error != null) {
                        if (error is KakaoSdkError && error.isInvalidTokenError()) {
                            // 로그인 필요
                            UserApiClient.instance.loginWithKakaoAccount(
                                requireContext(),
                                callback = callback
                            )
                        } else {
                            // 기타 에러
                            Log.e("LoginActivity", "error occured")
                            error.printStackTrace()
                        }
                    } else {
                        // 토큰 유효성 체크 성공
                        UserApiClient.instance.me { user, error ->
                            when {
                                error != null -> {
                                    Log.e(TAG, "사용자 정보 요청 실패: $error")
                                }
                                user != null -> {
                                    // MainActivity 로 이동
                                    successKakaoLogin(
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
                UserApiClient.instance.loginWithKakaoAccount(
                    requireContext(),
                    callback = callback
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.loginEvent.collect { event ->
                when (event) {
                    is LoginViewModel.LoginEvent.SuccessLogin -> {
                        Log.e(TAG, "retrofit success : ${event.responseBody}")
                        viewModel.updateToken(event.responseBody.jwt.token) // 토큰 dataStore 로 업데이트
                        if(event.responseBody.useYN == "Y"){
                            moveToEarlyBird()
                        } else {
                            moveToOnBoarding()
                        }
                    }
                    is LoginViewModel.LoginEvent.ErrorOccured -> {
                        Log.e(TAG, "retrofit error : ${event.errorBody.string()}")
                    }
                }
            }
        }
    }

    private fun googleLoginInit() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_server_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
        registerResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // Result OK 일 때, SignIn 한 결과에서 account 가져오기
                    val data = result.data
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        // success
                            Log.e("test", "success login")
                        val account = task.getResult(ApiException::class.java)
                        successGoogleLogin(account)
                    } catch (e: ApiException) {
                        // fail
                        Log.w(TAG, "signInResult:failed code= " + e.statusCode)
                    }
                }
            }
    }

    private fun kakaoLoginInit() {
        KakaoSdk.init(requireContext(), getString(R.string.native_app_key))
        callback = { token, error ->
            if (error != null) {
                Log.e(TAG, "로그인 실패 : ${error}")
            } else if (token != null) {
                Log.e(TAG, "로그인 성공")
                UserApiClient.instance.me { user, error ->
                    when {
                        error != null -> {
                            Log.e(TAG, "사용자 정보 요청 실패: ${error}")
                        }
                        user != null -> {
                            successKakaoLogin(user, token) // 로그인 성공
                        }
                        else -> {
                            Log.e(TAG, "user is null")
                        }
                    }
                }
            }
        }
    }

    private fun successGoogleLogin(account: GoogleSignInAccount) {
        // 구글 로그인 성공
        /*** kakao API response ***
         * accountName : account.account.name
         * accountType : account.account.type
         * displayName : account.displayName
         * email : account.email
         * givenName : account.givenName
         * id : account.id
         * photoUrl : account.photoUrl
         * idToken : account.idToken
         * isExpired : account.isExpired
         * *** ***************** ***/

        Log.e("test", "${account.email} ${account.displayName} ${account.idToken}")
        viewModel.loginGoogle(
            email = account.email,
            name = account.displayName,
            googleToken = account.idToken
        )
    }

    private fun successKakaoLogin(user: User, token: OAuthToken) {
        // 카카오 로그인 성공
        /*** kakao API response ***
         * id : user?.id
         * profileNickname : user?.kakaoAccount?.profile?.nickname
         * accountEmail : user?.kakaoAccount?.gender
         * birthday : user?.kakaoAccount?.ageRange
         * accessToken : token.accessToken
         * *** ***************** ***/

        viewModel.loginKakao(
            email = user?.kakaoAccount?.email.toString(),
            name = user?.kakaoAccount?.profile?.nickname.toString(),
            kakaoToken = token.accessToken
        )
    }

    private fun moveToOnBoarding() {
        // 온보딩 화면으로 이동
        parentActivity.supportFragmentManager.replaceFragment(OnbStartFragment(), false)
    }

    private fun moveToEarlyBird() {
        // 얼리버드 화면(메인화면)으로 이동
        parentActivity.supportFragmentManager.removeAllBackStack()
        parentActivity.supportFragmentManager.replaceFragment(MainFragment(), false)
    }
}