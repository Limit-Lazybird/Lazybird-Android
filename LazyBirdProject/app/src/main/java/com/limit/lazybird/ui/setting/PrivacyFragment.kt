package com.limit.lazybird.ui.setting

import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentPrivacyBinding
import com.limit.lazybird.ui.BaseFragment
import com.limit.lazybird.util.applyEscapeSequence
import dagger.hilt.android.AndroidEntryPoint

/***************** PrivacyFragment *******************
 * 메인화면(마이버드 탭) >> 옵션 >> 개인정보 처리방침 (Fragment)
 * 개인정보처리방침 보기
 ********************************************** ***/
@AndroidEntryPoint
class PrivacyFragment : BaseFragment<FragmentPrivacyBinding>(FragmentPrivacyBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this

        binding.privacyContext.text = getString(R.string.privacy_context).applyEscapeSequence()
        binding.privacyScrollView.fullScroll(ScrollView.FOCUS_DOWN) // 끝까지 스크롤 되도록 설정
    }

    // 뒤로가기 버튼 클릭 시
    fun clickBackBtn() {
        navController.popBackStack()
    }
}