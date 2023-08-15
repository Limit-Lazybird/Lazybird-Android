package com.limit.lazybird.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.limit.lazybird.R
import com.limit.lazybird.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/************* MainActivity ***************
 * 메인 Activity
 * Single Activity Architecture 인 이 프로젝트의 메인이 되는 Activity
 ********************************************** ***/
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val IS_DEV_MODE = "is_dev_mode"
    private val preferences by lazy { getSharedPreferences("dev_mode", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.findNavController()

        binding.isDevTextView.isVisible = preferences.getBoolean(IS_DEV_MODE, false)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}