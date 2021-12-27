package com.limit.lazybird.ui.splashlogin

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.limit.lazybird.R
import com.limit.lazybird.api.GoogleLoginHelper
import com.limit.lazybird.api.KakaoLoginHelper
import com.limit.lazybird.databinding.FragmentLoginBinding
import com.limit.lazybird.ui.BaseFragment
import com.limit.lazybird.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

/************* LoginFragment ***************
 * 로그인 화면 (Fragment)
 * 로그인 화면 (카카오 로그인, 구글 로그인)
 ********************************************** ***/
@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()

    // for kakao login
    private lateinit var kakaoLoginHelper: KakaoLoginHelper
    
    // for google login
    private lateinit var googleLoginHelper: GoogleLoginHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // kakao 개체 구성
        kakaoLoginHelper = KakaoLoginHelper(requireContext()).apply {
            // 카카오 로그인결과 업데이트
            loginInfo.observe(viewLifecycleOwner) {
                viewModel.loginKakao(
                    email = it.email,
                    name = it.name,
                    kakaoToken = it.token
                )
            }
        }

        // GoogleSignInClient 개체 구성
        googleLoginHelper = GoogleLoginHelper(this).apply {
            // 구글 로그인결과 업데이트
            loginInfo.observe(viewLifecycleOwner) {
                viewModel.loginGoogle(
                    email = it.email,
                    name = it.name,
                    googleToken = it.token
                )
            }
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
                            moveToEarlyBird() // 온보딩 완료된 사용자
                        } else {
                            moveToOnBoarding() // 온보딩 완료되지 않은 사용자
                        }
                    }
                    is LoginViewModel.LoginEvent.ErrorOccured -> {
                        Log.e(TAG, "retrofit error : ${event.errorBody.string()}")
                    }
                }
            }
        }
    }

    // 온보딩 화면으로 이동
    private fun moveToOnBoarding() {
        navController.navigate(LoginFragmentDirections.actionLoginFragmentToOnbStartFragment())
    }

    // 얼리버드 화면(메인화면)으로 이동
    private fun moveToEarlyBird() {
        navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
    }
}