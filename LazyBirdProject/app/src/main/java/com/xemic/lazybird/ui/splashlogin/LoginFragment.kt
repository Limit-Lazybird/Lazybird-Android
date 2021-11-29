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
import com.xemic.lazybird.api.GoogleLoginHelper
import com.xemic.lazybird.api.KakaoLoginHelper
import com.xemic.lazybird.databinding.FragmentLoginBinding
import com.xemic.lazybird.models.UserInfo
import com.xemic.lazybird.ui.MainActivity
import com.xemic.lazybird.ui.MainFragment
import com.xemic.lazybird.ui.onboarding.OnbStartFragment
import com.xemic.lazybird.util.removeAllBackStack
import com.xemic.lazybird.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

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
    private lateinit var kakaoLoginHelper: KakaoLoginHelper
    
    // for google login
    private lateinit var googleLoginHelper: GoogleLoginHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        // kakao 개체 구성
        kakaoLoginHelper = KakaoLoginHelper(requireContext()).apply {
            init()
        }

        // GoogleSignInClient 개체 구성
        googleLoginHelper = GoogleLoginHelper(this).apply {
            init()
        }

        binding.loginGoogleBtn.setOnClickListener {
            // 구글로 로그인하기 버튼 클릭
            googleLoginHelper.login()
        }

        binding.loginKakaoBtn.setOnClickListener {
            // 카카오로 로그인하기 버튼 클릭
            kakaoLoginHelper.login()
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

        kakaoLoginHelper.loginInfo.observe(viewLifecycleOwner) {
            viewModel.loginKakao(
                email = it.email,
                name = it.name,
                kakaoToken = it.token
            )
        }
        googleLoginHelper.loginInfo.observe(viewLifecycleOwner) {
            Log.e(TAG, "collect: ${it}")
            viewModel.loginGoogle(
                email = it.email,
                name = it.name,
                googleToken = it.token
            )
        }
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