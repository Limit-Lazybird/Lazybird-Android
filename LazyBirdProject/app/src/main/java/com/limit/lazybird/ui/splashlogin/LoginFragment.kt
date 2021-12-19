package com.limit.lazybird.ui.splashlogin

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.limit.lazybird.R
import com.limit.lazybird.api.GoogleLoginHelper
import com.limit.lazybird.api.KakaoLoginHelper
import com.limit.lazybird.databinding.FragmentLoginBinding
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.ui.MainFragment
import com.limit.lazybird.ui.onboarding.OnbStartFragment
import com.limit.lazybird.util.removeAllBackStack
import com.limit.lazybird.util.replaceFragment
import com.limit.lazybird.viewmodel.LoginViewModel
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

        // 구글로 로그인하기 버튼 클릭
        binding.loginGoogleBtn.setOnClickListener {
            googleLoginHelper.login()
        }

        // 카카오로 로그인하기 버튼 클릭
        binding.loginKakaoBtn.setOnClickListener {
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

        // 카카오 로그인결과 업데이트
        kakaoLoginHelper.loginInfo.observe(viewLifecycleOwner) {
            viewModel.loginKakao(
                email = it.email,
                name = it.name,
                kakaoToken = it.token
            )
        }

        // 구글 로그인결과 업데이트
        googleLoginHelper.loginInfo.observe(viewLifecycleOwner) {
            Log.e(TAG, "collect: ${it}")
            viewModel.loginGoogle(
                email = it.email,
                name = it.name,
                googleToken = it.token
            )
        }
    }

    // 온보딩 화면으로 이동
    private fun moveToOnBoarding() {
        parentActivity.supportFragmentManager.replaceFragment(OnbStartFragment(), false)
    }

    // 얼리버드 화면(메인화면)으로 이동
    private fun moveToEarlyBird() {
        parentActivity.supportFragmentManager.removeAllBackStack()
        parentActivity.supportFragmentManager.replaceFragment(MainFragment(), false)
    }
}