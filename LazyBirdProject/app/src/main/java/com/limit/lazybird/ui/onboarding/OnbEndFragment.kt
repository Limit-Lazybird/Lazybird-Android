package com.limit.lazybird.ui.onboarding

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.fragment.app.Fragment
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentOnbEndBinding
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.ui.MainFragment
import com.limit.lazybird.util.removeAllBackStack
import com.limit.lazybird.util.replaceFragment

/************* OnbEndFragment ***************
 * 온보딩 시작화면 >> 온보딩 화면 >> 온보딩 완료 화면 (Fragment)
 * 온보딩을 완료했을 때의 화면
 ********************************************** ***/
class OnbEndFragment : Fragment(R.layout.fragment_onb_end) {

    companion object {
        const val TAG = "OnbEndFragment"
    }

    private lateinit var binding: FragmentOnbEndBinding
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOnbEndBinding.bind(view)

        // TextView 에 일부 글자만 스타일 다르게 넣어주기
        binding.onbEndContext.text =
            SpannableStringBuilder(getString(R.string.onb_end_context)).apply {
                val startIdx = 9
                val endIdx = 29
                setSpan(
                    ForegroundColorSpan(resources.getColor(R.color.black, null)),
                    startIdx, // start
                    endIdx, // end
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                )
                setSpan(
                    BackgroundColorSpan(resources.getColor(R.color.or01, null)),
                    startIdx, // start
                    endIdx, // end
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(
                    StyleSpan(Typeface.BOLD),
                    startIdx, // start
                    endIdx, // end
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

        binding.orbEndStartBtn.setOnClickListener {
            // 시작하기 버튼 클릭
            moveToEarlyBird()
        }
    }

    private fun moveToEarlyBird() {
        // 얼리버드 화면(메인화면)으로 이동
        parentActivity.supportFragmentManager.removeAllBackStack()
        parentActivity.supportFragmentManager.replaceFragment(MainFragment(), false)
    }
}