package com.limit.lazybird.ui.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.limit.lazybird.BuildConfig
import com.limit.lazybird.R
import com.limit.lazybird.api.GoogleLoginHelper
import com.limit.lazybird.api.KakaoLoginHelper
import com.limit.lazybird.databinding.FragmentSettingBinding
import com.limit.lazybird.ui.BaseFragment
import com.limit.lazybird.ui.custom.dialog.ExhibitionRefreshBSDialog
import com.limit.lazybird.ui.custom.dialog.LogoutBSDialog
import com.limit.lazybird.viewmodel.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/***************** SettingFragment *******************
 * 메인화면(마이버드 탭) >> 옵션  (Fragment)
 * 옵션 화면
 ********************************************** ***/
@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {

    private val viewModel: SettingViewModel by viewModels()

    // for login
    private lateinit var kakaoLoginHelper: KakaoLoginHelper
    private lateinit var googleLoginHelper: GoogleLoginHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this

        kakaoLoginHelper = KakaoLoginHelper(requireContext())
        googleLoginHelper = GoogleLoginHelper(this)

        binding.settingOnbAppVersionText.text = "${BuildConfig.VERSION_NAME}v"

        // 성향 분석 재설정에서 Dialog 결과 반환
        setFragmentResultListener(ExhibitionRefreshBSDialog.TAG) { _, bundle ->
            when (bundle.getString(ExhibitionRefreshBSDialog.RESULT_CODE)) {
                ExhibitionRefreshBSDialog.RESULT_OK -> {
                    moveToOnb()
                }
            }
        }

        // 로그아웃 Dialog 결과 반환
        setFragmentResultListener(LogoutBSDialog.TAG) { _, bundle ->
            when (bundle.getString(LogoutBSDialog.RESULT_CODE)) {
                LogoutBSDialog.RESULT_OK -> {
                    // 로그아웃에서 OK 버튼 누름
                    lifecycleScope.launch {
                        viewModel.userInfo.collect { userInfo ->
                            when (userInfo.loginType) {
                                "google" -> googleLoginHelper.logout()
                                "kakao" -> kakaoLoginHelper.logout()
                            }
                        }
                    }
                    moveToLogin()
                }
            }
        }
    }

    // 온보딩 재설정 버튼 클릭 시
    fun clickOnbResetBtn() {
        ExhibitionRefreshBSDialog().show(
            parentFragmentManager,
            ExhibitionRefreshBSDialog.TAG
        )
    }

    // 로그아웃 버튼 클릭 시
    fun clickLogoutBtn() {
        LogoutBSDialog().show(parentFragmentManager, LogoutBSDialog.TAG)
    }

    // 뒤로가기 버튼 클릭 시
    fun clickBackBtn() {
        navController.popBackStack()
    }

    // 회원탈퇴 화면으로 이동
    fun moveToMemberOut() {
        navController.navigate(SettingFragmentDirections.actionSettingFragmentToMemberOutFragment())
    }

    // 공지사항 화면으로 이동
    fun moveToNotice() {
        navController.navigate(SettingFragmentDirections.actionSettingFragmentToNoticeFragment())
    }

    // 개인정보 처리방침 화면으로 이동
    fun moveToPrivacy() {
        navController.navigate(SettingFragmentDirections.actionSettingFragmentToPrivacyFragment())
    }

    // 이용약관 화면으로 이동
    fun moveToTerm() {
        navController.navigate(SettingFragmentDirections.actionSettingFragmentToTermFragment())
    }

    // 이용약관 화면으로 이동
    private fun moveToOnb() {
        navController.navigate(SettingFragmentDirections.actionSettingFragmentToOnbFragment())
    }

    private fun moveToLogin() {
        repeat(navController.currentBackStack.value.size) {
            navController.popBackStack()
        }
    }
}