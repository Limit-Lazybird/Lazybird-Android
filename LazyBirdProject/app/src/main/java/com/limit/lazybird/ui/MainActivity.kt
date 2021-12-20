package com.limit.lazybird.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.limit.lazybird.R
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
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.findNavController()

        // 첫 fragment 화면은 splashFragment 으로 설정
//        supportFragmentManager.replaceFragment(
//            SplashFragment(),
//            false
//        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}