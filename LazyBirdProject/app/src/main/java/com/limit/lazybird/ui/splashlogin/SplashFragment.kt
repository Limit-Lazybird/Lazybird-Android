package com.limit.lazybird.ui.splashlogin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentSplashBinding
import com.limit.lazybird.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/************* LoginFragment ***************
 * 로그인 화면 (Fragment)
 * 로그인 화면 (카카오 로그인, 구글 로그인)
 ********************************************** ***/
@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val SPLASH_DELAY_MILLIS = 3000L // 스플래시 화면에서 다음으로 넘어가는 시간

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            moveToLogin()
        }, SPLASH_DELAY_MILLIS)
    }

    // 로그인 화면으로 이동
    private fun moveToLogin() {
        navController.navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
    }
}