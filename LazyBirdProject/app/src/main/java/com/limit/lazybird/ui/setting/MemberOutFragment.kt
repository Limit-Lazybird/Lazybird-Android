package com.limit.lazybird.ui.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.limit.lazybird.R
import com.limit.lazybird.api.GoogleLoginHelper
import com.limit.lazybird.api.KakaoLoginHelper
import com.limit.lazybird.databinding.FragmentMemberOutBinding
import com.limit.lazybird.ui.BaseFragment
import com.limit.lazybird.util.applyEscapeSequence
import com.limit.lazybird.viewmodel.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/************* MemberOutFragment ***************
 * 메인화면(마이버드 탭) >> 옵션 >> 회원탈퇴 (Fragment)
 * 정말로 회원탈퇴 할지 선택하는 화면
 ********************************************** ***/
@AndroidEntryPoint
class MemberOutFragment :
    BaseFragment<FragmentMemberOutBinding>(FragmentMemberOutBinding::inflate) {

    private val viewModel: SettingViewModel by viewModels()

    // for google login
    private lateinit var googleLoginHelper: GoogleLoginHelper
    private lateinit var kakaoLoginHelper: KakaoLoginHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this

        kakaoLoginHelper = KakaoLoginHelper(requireContext())
        googleLoginHelper = GoogleLoginHelper(this)

        binding.memberOutContext.text = getString(R.string.member_out_context).applyEscapeSequence()
    }

    fun clickMemberOutBtn() {
        // 클라이언트 단에서 끊어주기
        lifecycleScope.launch {
            viewModel.userInfo.collect { userInfo ->
                when (userInfo.loginType) {
                    "google" -> googleLoginHelper.memberOut()
                    "kakao" -> kakaoLoginHelper.memberOut()
                }
            }
        }
        viewModel.deleteUser() // 서버단에서 끊어주기
        moveToLogin() // 로그인 화면으로 이동
    }

    // 뒤로가기 버튼 클릭 시
    fun clickBackBtn() {
        navController.popBackStack()
    }

    // 뒤로가기 버튼 클릭 시
    private fun moveToLogin() {
        repeat(navController.currentBackStack.value.size) {
            navController.popBackStack()
        }
    }
}