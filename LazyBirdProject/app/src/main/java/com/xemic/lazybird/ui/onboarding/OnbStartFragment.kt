package com.xemic.lazybird.ui.onboarding

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.FragmentOnbStartBinding
import com.xemic.lazybird.ui.MainActivity
import com.xemic.lazybird.ui.MainFragment
import com.xemic.lazybird.ui.earlybird.EarlyBirdFragment
import com.xemic.lazybird.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnbStartFragment : Fragment(R.layout.fragment_onb_start) {

    private lateinit var binding: FragmentOnbStartBinding

    private val viewModel: OnbStartViewModel by viewModels()

    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    // 선택한 결과 (-1로 초기화)

    /*** deprecated ***/
//    private var page = 0 // 현재 페이지

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOnbStartBinding.bind(view)

        binding.onbsOkBtn.setOnClickListener {
            // 전시성향분석 버튼 클릭
//            openCurrentOnbDialog(page) // deprecated
            parentActivity.supportFragmentManager.replaceFragment(OnbFragment(), true)
        }
        binding.onbsToBeNextBtn.setOnClickListener {
            // 다음에 할래요 버튼 클릭
            moveToEarlyBird()
        }

        /*** deprecated ***/
//        setFragmentResultListener(OnbDialogFragment.TAG) { _, bundle ->
//            when (bundle.getString("resultCode")) {
//                OnbDialogFragment.RESULT_MOVE_NEXT -> {
//                    // 다음 페이지로 이동
//                    val selectedNo = bundle.getInt(OnbDialogFragment.SELECT_NO)
//                    selectedResult[page] = selectedNo
//                    openNextOnbDialog()
//                }
//                OnbDialogFragment.RESULT_MOVE_PREV -> {
//                    // 이전 페이지로 이동
//                    openPrevOnbDialog()
//                }
//            }
//        }

    }

    /*** deprecated ***/
//    private fun openNextOnbDialog() {
//        // 다음페이지 dialog 띄우기
//        if(page < viewModel.onbSurveyList.size)
//            ++page
//        openCurrentOnbDialog(page)
//    }
//
//    private fun openPrevOnbDialog() {
//        // 이전페이지 dialog 띄우기
//        if(page > 0)
//            --page
//        openCurrentOnbDialog(page)
//    }
//
//    private fun openCurrentOnbDialog(_page: Int) {
//        // 온보딩 선택화면 열기
//        if (_page >= viewModel.onbSurveyList.size) {
//            parentActivity.supportFragmentManager.replaceFragment(OnbEndFragment(), false)
//            for(idx in selectedResult.indices){
//                // Todo : for test (deleted someday)
//                Log.e("Test", "${idx} page selected : ${selectedResult[idx]}")
//            }
//        } else {
//            // 문항 Dialog 생성
//            val dialog = OnbDialogFragment().apply {
//                arguments = Bundle().apply {
//                    putParcelable(OnbDialogFragment.SURVEY, viewModel.onbSurveyList[_page])
//                    putInt(OnbDialogFragment.PAGE, _page)
//                }
//            }
//            dialog.show(parentActivity.supportFragmentManager, OnbDialogFragment.TAG)
//        }
//    }

    private fun moveToEarlyBird() {
        // 얼리버드 화면(메인화면)으로 이동
        parentActivity.supportFragmentManager.replaceFragment(MainFragment(), false)
    }
}