package com.xemic.lazybird.ui.setting

import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.FragmentPrivacyBinding
import com.xemic.lazybird.databinding.FragmentTermBinding
import com.xemic.lazybird.ui.MainActivity
import com.xemic.lazybird.util.applyEscapeSequence
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyFragment : Fragment(R.layout.fragment_privacy) {

    lateinit var binding: FragmentPrivacyBinding
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrivacyBinding.bind(view)
        binding.privacyBackBtn.setOnClickListener {
            // 뒤로가기 버튼
            parentActivity.supportFragmentManager.popBackStack()
        }
        binding.privacyContext.text = getString(R.string.privacy_context).applyEscapeSequence()
        binding.privacyScrollView.fullScroll(ScrollView.FOCUS_DOWN) // 끝까지 스크롤 되도록 설정
    }
}