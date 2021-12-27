package com.limit.lazybird.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentMainBinding
import com.limit.lazybird.models.DialogInfo
import com.limit.lazybird.models.DialogResult
import com.limit.lazybird.ui.calendar.CalendarFragment
import com.limit.lazybird.ui.earlybird.EarlyBirdFragment
import com.limit.lazybird.ui.exhibition.ExhibitionFragment
import com.limit.lazybird.ui.mybird.MyBirdFragment
import com.limit.lazybird.ui.custom.dialog.CustomDialogFragment
import com.limit.lazybird.ui.search.SearchFragment
import com.limit.lazybird.util.replaceChildFragment

/************* ExhibitionViewModel ***************
 * 메인화면 (Fragment)
 * (얼리버드, 전시, 캘린더, 검색, 마이버드 탭) 을 모두 포함하는 Fragment
 ********************************************** ***/
class MainFragment: BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private var _currentChildFragment = MutableLiveData(0) // 선택된 페이지
    private val currentChildFragment: LiveData<Int> get() = _currentChildFragment

    lateinit var callback: OnBackPressedCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        navController.currentBackStackEntry?.savedStateHandle?.apply {
            getLiveData<DialogResult>(CustomDialogFragment.TAG)?.observe(viewLifecycleOwner) { dialogResult ->
                when(dialogResult.results[0]){
                    CustomDialogFragment.RESULT_OK -> {
                        requireActivity().finish()
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
    
    // bottomNav 변화에 따른 ChildFragment 변화
    private fun updateChildFragment(page: Int) {
        if(_currentChildFragment.value != page)
            _currentChildFragment.value = page // 페이지가 동일할 땐 화면을 변경하지 않는다.
    }

    // 레이지버드 종료 Dialog 보여주기
    private fun showDialog() {
        val dialogInfo = DialogInfo(
            title = resources.getString(R.string.main_exit_title),
            message = "",
            positiveBtnTitle = resources.getString(R.string.main_exit_yes),
            negativeBtnTitle = resources.getString(R.string.main_exit_no)
        )
        val action = MainFragmentDirections.actionMainFragmentToCustomDialogFragment(dialogInfo)
        navController.navigate(action)
    }
}