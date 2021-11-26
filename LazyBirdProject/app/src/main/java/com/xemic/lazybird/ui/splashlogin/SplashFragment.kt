package com.xemic.lazybird.ui.splashlogin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.kakao.sdk.common.util.Utility
import com.xemic.lazybird.R
import com.xemic.lazybird.ui.MainActivity
import com.xemic.lazybird.util.replaceFragment

class SplashFragment : Fragment(R.layout.fragment_splash) {
    companion object{
        const val TAG = "SplashFragment"
    }
    private val SPLASH_DELAY_MILLIS = 3000L
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            moveToLogin(view)
        }, SPLASH_DELAY_MILLIS)
    }

    private fun moveToLogin(view: View) {
        // 온보딩 화면으로 이동
        parentActivity.supportFragmentManager.replaceFragment(LoginFragment(), false)
    }
}