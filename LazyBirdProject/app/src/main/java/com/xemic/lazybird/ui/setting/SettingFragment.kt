package com.xemic.lazybird.ui.setting

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.user.UserApiClient
import com.xemic.lazybird.R
import com.xemic.lazybird.api.KakaoLoginHelper
import com.xemic.lazybird.data.PreferenceDataStoreManager
import com.xemic.lazybird.databinding.FragmentSettingBinding
import com.xemic.lazybird.ui.MainActivity
import com.xemic.lazybird.ui.exhibition.ExhibitionRefreshBSDialog
import com.xemic.lazybird.ui.onboarding.OnbFragment
import com.xemic.lazybird.ui.splashlogin.LoginFragment
import com.xemic.lazybird.util.removeAllBackStack
import com.xemic.lazybird.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/***************** SettingFragment *******************
 * 메인화면(마이버드 탭) >> 옵션  (Fragment)
 * 옵션 화면
 * Todo : google 로그아웃 구현하기
 ********************************************** ***/
@AndroidEntryPoint
class SettingFragment : Fragment(R.layout.fragment_setting) {

    companion object {
        const val TAG = "SettingFragment"
    }

    lateinit var binding: FragmentSettingBinding
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingBinding.bind(view)

        binding.settingBackBtn.setOnClickListener {
            // 뒤로가기 버튼 클릭
            parentActivity.supportFragmentManager.popBackStack()
        }

        binding.settingOnbReset.setOnClickListener {
            // 성향분석 재설정 버튼 클릭
            ExhibitionRefreshBSDialog().show(
                parentFragmentManager,
                ExhibitionRefreshBSDialog.TAG
            )
        }

        binding.settingOnbNotice.setOnClickListener {
            // 공지사항 버튼 클릭
            parentActivity.supportFragmentManager.replaceFragment(NoticeFragment())
        }

        binding.settingOnbTerm.setOnClickListener {
            // 이용약관 버튼 클릭
            parentActivity.supportFragmentManager.replaceFragment(TermFragment())
        }

        binding.settingOnbPrivacyRule.setOnClickListener {
            // 개인정보 처리방침 버튼 클릭
            parentActivity.supportFragmentManager.replaceFragment(PrivacyFragment())
        }

        binding.settingOnbLogout.setOnClickListener {
            // 로그아웃 버튼 클릭
            LogoutBSDialog().show(parentFragmentManager, LogoutBSDialog.TAG)
        }

        binding.settingOnbMemberOut.setOnClickListener {
            // 회원탈퇴 버튼 클릭
            parentActivity.supportFragmentManager.replaceFragment(MemberOutFragment())
        }

        setFragmentResultListener(ExhibitionRefreshBSDialog.TAG) { _, bundle ->
            when (bundle.getString(ExhibitionRefreshBSDialog.RESULT_CODE)) {
                ExhibitionRefreshBSDialog.RESULT_OK -> {
                    // 성향 분석 재설정에서 OK 버튼 누름
                    parentActivity.supportFragmentManager.replaceFragment(OnbFragment())
                }
            }
        }

        setFragmentResultListener(LogoutBSDialog.TAG) { _, bundle ->
            when (bundle.getString(LogoutBSDialog.RESULT_CODE)) {
                LogoutBSDialog.RESULT_OK -> {
                    // 로그아웃에서 OK 버튼 누름
                    KakaoLoginHelper(requireContext()).logout()
                    parentActivity.supportFragmentManager.removeAllBackStack()
                    parentActivity.supportFragmentManager.replaceFragment(LoginFragment(), false)
                }
            }
        }
    }
}