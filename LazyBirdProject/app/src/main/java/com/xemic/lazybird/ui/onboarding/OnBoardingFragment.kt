package com.xemic.lazybird.ui.onboarding

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.xemic.lazybird.R
import com.xemic.lazybird.ui.MainActivity
import com.xemic.lazybird.ui.earlybird.EarlyBirdFragment
import com.xemic.lazybird.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_on_boarding.*

/*** deprecated ***/

@AndroidEntryPoint
class OnBoardingFragment : Fragment(R.layout.fragment_on_boarding) {
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }
    private val onBoardingViewModel: OnBoardingViewModel by viewModels()
    private val onBoardingAdapter: OnBoardingAdapter by lazy {
        OnBoardingAdapter(listOf(), viewLifecycleOwner)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        on_boarding_categories.apply {
            adapter = onBoardingAdapter
        }

        onBoardingAdapter.itemClickListener = object : OnBoardingAdapter.OnItemClickListener {
            override fun onItemClick(
                holder: OnBoardingAdapter.ViewHolder,
                view: View,
                position: Int,
                idx: Int
            ) {
                onBoardingViewModel.optionItemClicked(view, position, idx)
            }
        }

        onBoardingViewModel.exhibitionCategories.observe(viewLifecycleOwner) { exhibitionCategoryList ->
            onBoardingAdapter.setCategoryList(exhibitionCategoryList)
        }

        on_boarding_to_be_next_btn.setOnClickListener {
            // 다음에 하기 버튼 클릭
            // 선택된 목록들 가져오기
            val selectedCategoryList = mutableListOf<List<String>>()
            val categories = onBoardingViewModel.exhibitionCategories.value!!
            for (i in categories.indices) {
                val selectedOptionList = mutableListOf<String>()
                for (option in categories[i].categoryList) {
                    if (option.isSelected)
                        selectedOptionList.add(option.optionName)
                }
                selectedCategoryList.add(selectedOptionList)
            }

            // Todo : TEST
            for (category in selectedCategoryList) {
                Log.e("test", "=================")
                for (selectedString in category) {
                    Log.e("test", "${selectedString}")
                }
            }

            // EarlyBird 화면으로 이동
            parentActivity.supportFragmentManager.popBackStack() // 이전 stack 지우기
            moveToEarlyBird()
        }
        on_boarding_back_btn.setOnClickListener {
            // 뒤로가기 버튼 클릭
            parentActivity.supportFragmentManager.popBackStack() // 뒤로가기
        }
    }

    private fun moveToEarlyBird() {
        // 얼리버드 화면(메인화면)으로 이동
        parentActivity.supportFragmentManager.replaceFragment(EarlyBirdFragment(), false)
    }
}