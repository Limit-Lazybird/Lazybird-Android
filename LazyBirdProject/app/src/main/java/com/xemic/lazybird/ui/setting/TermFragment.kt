package com.xemic.lazybird.ui.setting

import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.FragmentTermBinding
import com.xemic.lazybird.ui.MainActivity
import com.xemic.lazybird.util.applyEscapeSequence
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermFragment:Fragment(R.layout.fragment_term) {

    lateinit var binding :FragmentTermBinding
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTermBinding.bind(view)
        binding.termBackBtn.setOnClickListener {
            // 뒤로가기 버튼
            parentActivity.supportFragmentManager.popBackStack()
        }
        binding.termContext.text = getString(R.string.term_context).applyEscapeSequence()
        binding.termScrollView.fullScroll(ScrollView.FOCUS_DOWN) // 끝까지 스크롤 되도록 설정
    }
}