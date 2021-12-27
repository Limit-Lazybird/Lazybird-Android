package com.limit.lazybird.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentOnbStartBinding
import com.limit.lazybird.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/************* OnbStartFragment ***************
 * 온보딩 시작 화면 (Fragment)
 * 온보딩 설문조사하기 전의 시작화면
 ********************************************** ***/
@AndroidEntryPoint
class OnbStartFragment : BaseFragment<FragmentOnbStartBinding>(FragmentOnbStartBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment = this
    }

    // 얼리버드 화면(메인화면)으로 이동
    fun moveToEarlyBird() {
        navController.navigate(OnbStartFragmentDirections.actionOnbStartFragmentToMainFragment())
    }

    // 온보딩 화면으로 이동
    fun moveToOnb() {
        navController.navigate(OnbStartFragmentDirections.actionOnbStartFragmentToOnbFragment())
    }
}