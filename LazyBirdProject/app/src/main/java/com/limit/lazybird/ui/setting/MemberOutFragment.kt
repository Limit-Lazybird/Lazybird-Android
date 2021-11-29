package com.limit.lazybird.ui.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.limit.lazybird.R
import com.limit.lazybird.api.GoogleLoginHelper
import com.limit.lazybird.api.KakaoLoginHelper
import com.limit.lazybird.databinding.FragmentMemberOutBinding
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.ui.splashlogin.LoginFragment
import com.limit.lazybird.util.applyEscapeSequence
import com.limit.lazybird.util.removeAllBackStack
import com.limit.lazybird.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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