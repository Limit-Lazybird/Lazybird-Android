package com.limit.lazybird.ui.setting

import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentTermBinding
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.util.applyEscapeSequence
import dagger.hilt.android.AndroidEntryPoint

/***************** PrivacyFragment *******************
 * 메인화면(마이버드 탭) >> 옵션 >> 이용약관 (Fragment)
 * 이용약관 보기
 ********************************************** ***/
@AndroidEntryPoint
class TermFragment:Fragment(R.layout.fragment_term) {

    companion object {
        const val TAG = "TermFragment"
    }

    lateinit var binding :FragmentTermBinding
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTermBinding.bind(view)

        // 뒤로가기 버튼
        binding.termBackBtn.setOnClickListener {
            parentActivity.supportFragmentManager.popBackStack()
        }

        binding.termContext.text = getString(R.string.term_context).applyEscapeSequence()
        binding.termScrollView.fullScroll(ScrollView.FOCUS_DOWN) // 끝까지 스크롤 되도록 설정
    }
}