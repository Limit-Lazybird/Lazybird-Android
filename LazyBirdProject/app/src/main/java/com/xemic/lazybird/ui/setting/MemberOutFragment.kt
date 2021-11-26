package com.xemic.lazybird.ui.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.FragmentMemberOutBinding
import com.xemic.lazybird.ui.MainActivity
import com.xemic.lazybird.ui.splashlogin.LoginFragment
import com.xemic.lazybird.util.applyEscapeSequence
import com.xemic.lazybird.util.removeAllBackStack
import com.xemic.lazybird.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemberOutFragment : Fragment(R.layout.fragment_member_out) {

    lateinit var binding: FragmentMemberOutBinding
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMemberOutBinding.bind(view)
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
            parentActivity.supportFragmentManager.removeAllBackStack()
            parentActivity.supportFragmentManager.replaceFragment(LoginFragment())
        }
        binding.memberOutContext.text = getString(R.string.member_out_context).applyEscapeSequence()
    }
}