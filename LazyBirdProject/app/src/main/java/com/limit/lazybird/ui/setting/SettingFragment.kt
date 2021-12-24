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
class SettingFragment : Fragment(R.layout.fragment_setting) {

    companion object {
        const val TAG = "SettingFragment"
    }

    private lateinit var navController: NavController
    lateinit var binding: FragmentSettingBinding
    private val viewModel: SettingViewModel by viewModels()

    // for login
    private lateinit var kakaoLoginHelper: KakaoLoginHelper
    private lateinit var googleLoginHelper: GoogleLoginHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = requireView().findNavController()
        binding = FragmentSettingBinding.bind(view)

        kakaoLoginHelper = KakaoLoginHelper(requireContext()).apply {
            init()
        }
        googleLoginHelper = GoogleLoginHelper(this).apply {
            init()
        }

        binding.settingOnbAppVersionText.text = "${BuildConfig.VERSION_NAME}v"

        // 뒤로가기 버튼 클릭
        binding.settingBackBtn.setOnClickListener {
            clickBackBtn()
        }

        // 성향분석 재설정 버튼 클릭
        binding.settingOnbReset.setOnClickListener {
            ExhibitionRefreshBSDialog().show(
                parentFragmentManager,
                ExhibitionRefreshBSDialog.TAG
            )
        }

        // 공지사항 버튼 클릭
        binding.settingOnbNotice.setOnClickListener {
            moveToNotice()
        }

        // 이용약관 버튼 클릭
        binding.settingOnbTerm.setOnClickListener {
            moveToTerm()
        }

        // 개인정보 처리방침 버튼 클릭
        binding.settingOnbPrivacyRule.setOnClickListener {
            moveToPrivacy()
        }

        // 회원탈퇴 버튼 클릭
        binding.settingOnbMemberOut.setOnClickListener {
            moveToMemberOut()
        }

        // 로그아웃 버튼 클릭
        binding.settingOnbLogout.setOnClickListener {
            LogoutBSDialog().show(parentFragmentManager, LogoutBSDialog.TAG)
        }

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
                            when(userInfo.loginType){
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

    // 뒤로가기 버튼 클릭 시
    private fun clickBackBtn() {
        navController.popBackStack()
    }
    
    // 회원탈퇴 화면으로 이동
    private fun moveToMemberOut(){
        navController.navigate(SettingFragmentDirections.actionSettingFragmentToMemberOutFragment())
    }

    // 공지사항 화면으로 이동
    private fun moveToNotice(){
        navController.navigate(SettingFragmentDirections.actionSettingFragmentToNoticeFragment())
    }

    // 개인정보 처리방침 화면으로 이동
    private fun moveToPrivacy(){
        navController.navigate(SettingFragmentDirections.actionSettingFragmentToPrivacyFragment())
    }

    // 이용약관 화면으로 이동
    private fun moveToTerm(){
        navController.navigate(SettingFragmentDirections.actionSettingFragmentToTermFragment())
    }

    // 이용약관 화면으로 이동
    private fun moveToOnb(){
        navController.navigate(SettingFragmentDirections.actionSettingFragmentToOnbFragment())
    }

    private fun moveToLogin(){
        repeat(navController.backStack.size){
            navController.popBackStack()
        }
    }
}