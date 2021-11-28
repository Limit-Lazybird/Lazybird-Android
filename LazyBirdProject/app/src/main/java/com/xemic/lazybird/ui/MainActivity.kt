package com.xemic.lazybird.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xemic.lazybird.databinding.ActivityMainBinding
import com.xemic.lazybird.ui.splashlogin.SplashFragment
import com.xemic.lazybird.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint

/************* MainActivity ***************
 * 메인 Activity
 * Single Activity Architecture 인 이 프로젝트의 메인이 되는 Activity
 ********************************************** ***/
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 첫 fragment 화면은 splashFragment 으로 설정
        supportFragmentManager.replaceFragment(
            SplashFragment(),
            false
        )
    }
}