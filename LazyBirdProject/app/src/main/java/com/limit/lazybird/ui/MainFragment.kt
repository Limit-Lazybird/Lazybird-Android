package com.limit.lazybird.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentMainBinding
import com.limit.lazybird.models.DialogInfo
import com.limit.lazybird.ui.calendar.CalendarFragment
import com.limit.lazybird.ui.earlybird.EarlyBirdFragment
import com.limit.lazybird.ui.exhibition.ExhibitionFragment
import com.limit.lazybird.ui.mybird.MyBirdFragment
import com.limit.lazybird.ui.onboarding.CustomDialogFragment
import com.limit.lazybird.ui.search.SearchFragment
import com.limit.lazybird.util.replaceChildFragment

/************* ExhibitionViewModel ***************
 * 메인화면 (Fragment)
 * (얼리버드, 전시, 캘린더, 검색, 마이버드 탭) 을 모두 포함하는 Fragment
 ********************************************** ***/
class MainFragment: Fragment(R.layout.fragment_main) {

    private var _currentChildFragment = MutableLiveData(0) // 선택된 페이지
    private val currentChildFragment: LiveData<Int> get() = _currentChildFragment

    private lateinit var binding : FragmentMainBinding
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    lateinit var callback: OnBackPressedCallback

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

        setFragmentResultListener(CustomDialogFragment.TAG) { _, bundle ->
            when (bundle.getString(CustomDialogFragment.RESULT_CODE)) {
                CustomDialogFragment.RESULT_OK -> {
                    parentActivity.finish()
                }
            }
        }

    }

    private fun updateChildFragment(page: Int) {
        if(_currentChildFragment.value != page)
            _currentChildFragment.value = page // 페이지가 동일할 땐 화면을 변경하지 않는다.
    }

    private fun showDialog() {
        val dialogInfo = DialogInfo(
            title = resources.getString(R.string.main_exit_title),
            message = "",
            positiveBtnTitle = resources.getString(R.string.main_exit_yes),
            negativeBtnTitle = resources.getString(R.string.main_exit_no)
        )
        CustomDialogFragment().apply {
            // dialog 정보 보내주기
            arguments = bundleOf().apply {
                putParcelable(CustomDialogFragment.DIALOG_INFO, dialogInfo)
            }
        }.show(
            parentActivity.supportFragmentManager,
            CustomDialogFragment.TAG
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(parentActivity.supportFragmentManager.backStackEntryCount == 0){
                    showDialog()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}