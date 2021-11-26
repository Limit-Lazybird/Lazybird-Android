package com.xemic.lazybird.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.FragmentMainBinding
import com.xemic.lazybird.ui.calendar.CalendarFragment
import com.xemic.lazybird.ui.earlybird.EarlyBirdFragment
import com.xemic.lazybird.ui.exhibition.ExhibitionFragment
import com.xemic.lazybird.ui.mybird.MyBirdFragment
import com.xemic.lazybird.ui.search.SearchFragment
import com.xemic.lazybird.util.replaceChildFragment

class MainFragment: Fragment(R.layout.fragment_main) {

    private lateinit var binding : FragmentMainBinding
    private var _currentChildFragment = MutableLiveData(0) // 선택된 페이지
    private val currentChildFragment: LiveData<Int> get() = _currentChildFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainBinding.bind(view)

        currentChildFragment.observe(viewLifecycleOwner) { page ->
            when(page) {
                0 -> childFragmentManager.replaceChildFragment(EarlyBirdFragment()) // 얼리버드 Fragment
                1 -> childFragmentManager.replaceChildFragment(ExhibitionFragment()) // 전시 Fragment
                2 -> childFragmentManager.replaceChildFragment(CalendarFragment()) // 캘린더 Fragment
                3 -> childFragmentManager.replaceChildFragment(SearchFragment()) // 검색 Fragment
                4 -> childFragmentManager.replaceChildFragment(MyBirdFragment()) // 마이버드 Fragment
            }
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            // bottom navigation 클릭 시
            when (it.itemId) {
                R.id.bnv_earlybird -> {
                    // 얼리버드 버튼 클릭
                    updateChildFragment(0)
                    return@setOnItemSelectedListener true
                }
                R.id.bnv_exhibition -> {
                    // 전시 버튼 클릭
                    updateChildFragment(1)
                    return@setOnItemSelectedListener true
                }
                R.id.bnv_calendar -> {
                    // 캘린더 버튼 클릭
                    updateChildFragment(2)
                    return@setOnItemSelectedListener true
                }
                R.id.bnv_search -> {
                    // 검색 버튼 클릭
                    updateChildFragment(3)
                    return@setOnItemSelectedListener true
                }
                R.id.bnv_mybird -> {
                    // 마이버드 버튼 클릭
                    updateChildFragment(4)
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }
    }

    private fun updateChildFragment(page: Int) {
        if(_currentChildFragment.value != page)
            _currentChildFragment.value = page // 페이지가 동일할 땐 화면을 변경하지 않는다.
    }
}