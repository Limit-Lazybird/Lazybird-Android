package com.limit.lazybird.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentOnbStartBinding
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.ui.MainFragment
import com.limit.lazybird.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint

/************* OnbStartFragment ***************
 * 온보딩 시작 화면 (Fragment)
 * 온보딩 설문조사하기 전의 시작화면
 ********************************************** ***/
@AndroidEntryPoint
class OnbStartFragment : Fragment(R.layout.fragment_onb_start) {

    companion object {
        const val TAG = "OnbStartFragment"
    }

    private lateinit var navController: NavController
    private lateinit var binding: FragmentOnbStartBinding
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = requireView().findNavController()
        binding = FragmentOnbStartBinding.bind(view)

        // 전시성향분석 버튼 클릭
        binding.onbsOkBtn.setOnClickListener {
            moveToOnb()
        }

        // 다음에 할래요 버튼 클릭
        binding.onbsToBeNextBtn.setOnClickListener {
            moveToEarlyBird()
        }
    }

    // 얼리버드 화면(메인화면)으로 이동
    private fun moveToEarlyBird() {
        navController.navigate(OnbStartFragmentDirections.actionOnbStartFragmentToMainFragment())
//        navController.navigate(R.id.action_onbStartFragment_to_mainFragment)
//        parentActivity.supportFragmentManager.replaceFragment(MainFragment(), false)
    }

    // 온보딩 화면으로 이동
    private fun moveToOnb() {
        navController.navigate(OnbStartFragmentDirections.actionOnbStartFragmentToOnbFragment())
//        navController.navigate(R.id.action_onbStartFragment_to_onbFragment)
//        parentActivity.supportFragmentManager.replaceFragment(OnbFragment(), true)
    }
}