package com.xemic.lazybird.ui.setting

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.user.UserApiClient
import com.xemic.lazybird.R
import com.xemic.lazybird.api.GoogleLoginHelper
import com.xemic.lazybird.api.KakaoLoginHelper
import com.xemic.lazybird.data.PreferenceDataStoreManager
import com.xemic.lazybird.databinding.FragmentMemberOutBinding
import com.xemic.lazybird.ui.MainActivity
import com.xemic.lazybird.ui.splashlogin.LoginFragment
import com.xemic.lazybird.util.applyEscapeSequence
import com.xemic.lazybird.util.removeAllBackStack
import com.xemic.lazybird.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/************* MemberOutFragment ***************
 * 메인화면(마이버드 탭) >> 옵션 >> 회원탈퇴 (Fragment)
 * 정말로 회원탈퇴 할지 선택하는 화면
 ********************************************** ***/
@AndroidEntryPoint
class MemberOutFragment : Fragment(R.layout.fragment_member_out) {

    companion object {
        const val TAG = "MemberOutFragment"
    }

    lateinit var binding: FragmentMemberOutBinding
    private val viewModel: SettingViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    // for google login
    private lateinit var googleLoginHelper: GoogleLoginHelper
    private lateinit var kakaoLoginHelper: KakaoLoginHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMemberOutBinding.bind(view)

        kakaoLoginHelper = KakaoLoginHelper(requireContext()).apply {
            init()
        }
        googleLoginHelper = GoogleLoginHelper(this).apply {
            init()
        }

        binding.memberOutBackBtn.setOnClickListener {
            // 뒤로가기 버튼
            parentActivity.supportFragmentManager.popBackStack()
        }
        binding.memberOutCancel.setOnClickListener {
            // 취소버튼
            parentActivity.supportFragmentManager.popBackStack()
        }
        binding.memberOutOk.setOnClickListener {
            // 탈퇴하기 버튼
            lifecycleScope.launch {
                viewModel.userInfo.collect { userInfo ->
                    when(userInfo.loginType){
                        "google" -> googleLoginHelper.memberOut()
                        "kakao" -> kakaoLoginHelper.memberOut()
                    }
                }
            }
            parentActivity.supportFragmentManager.removeAllBackStack()
            parentActivity.supportFragmentManager.replaceFragment(LoginFragment(), false)
        }
        binding.memberOutContext.text = getString(R.string.member_out_context).applyEscapeSequence()
    }
}