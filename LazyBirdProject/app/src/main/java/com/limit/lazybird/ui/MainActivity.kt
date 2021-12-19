package com.limit.lazybird.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.limit.lazybird.databinding.ActivityMainBinding
import com.limit.lazybird.ui.splashlogin.SplashFragment
import com.limit.lazybird.util.replaceFragment
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