package com.limit.lazybird.ui.splashlogin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import com.limit.lazybird.R
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.util.replaceFragment

/************* LoginFragment ***************
 * 로그인 화면 (Fragment)
 * 로그인 화면 (카카오 로그인, 구글 로그인)
 ********************************************** ***/
class SplashFragment : Fragment(R.layout.fragment_splash) {

    companion object{
        const val TAG = "SplashFragment"
    }
    
    private val SPLASH_DELAY_MILLIS = 3000L // 스플래시 화면에서 다음으로 넘어가는 시간
    
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            moveToLogin()
        }, SPLASH_DELAY_MILLIS)
    }

    // 로그인 화면으로 이동
    private fun moveToLogin() {
        parentActivity.supportFragmentManager.replaceFragment(LoginFragment(), false)
    }
}