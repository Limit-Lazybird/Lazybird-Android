package com.limit.lazybird.ui.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentCalendarAddBinding
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.ui.exhibition.ExhibitionFilterBSDialog
import com.limit.lazybird.ui.exhibition.ExhibitionRefreshBSDialog
import com.limit.lazybird.ui.onboarding.OnbFragment
import com.limit.lazybird.util.replaceFragment

class CalendarAddFragment: Fragment(R.layout.fragment_calendar_add) {

    companion object {
        const val TAG = "CalendarAddFragment"
        const val ADD_TYPE = "type"
        const val TYPE_CUSTOM = "custom"
        const val TYPE_TICKETED = "ticketed"
    }

    private lateinit var binding: FragmentCalendarAddBinding
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    private var type = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalendarAddBinding.bind(view)

        type = arguments?.getString(ADD_TYPE).toString() // custom or ticketed
        if(type == TYPE_TICKETED) {
            // Todo : 전시, 장소 argument 로 받아서 editText 에 넣고 수정불가로 넣어주기.
        }

        binding.calendarAddBackBtn.setOnClickListener {
            parentActivity.supportFragmentManager.popBackStack()
        }

        binding.calendarAddDate.setOnClickListener { 
            // 날짜 버튼 클릭
            // Todo : why transperent 적용 안되었지..?
            DateSelectBSDialog().show(
                parentFragmentManager,
                DateSelectBSDialog.TAG
            )
        }
        
        binding.calendarAddTimeStart.setOnClickListener { 
            // 시작시간 버튼 클릭
        }
        
        binding.calendarAddTimeEnd.setOnClickListener { 
            // 끝시간 버튼 클릭
        }

        binding.calendarAddDeleteBtn.setOnClickListener {
            // 삭제 버튼
            when(type){
                TYPE_CUSTOM -> {

                }
                TYPE_TICKETED -> {

                }
            }
        }

        binding.calendarAddOkBtn.setOnClickListener {
            // 추가 버튼

        }

        setFragmentResultListener(DateSelectBSDialog.TAG) { _, bundle ->
            // 날짜 선택 Dialog 결과 확인
            when(bundle.getString(DateSelectBSDialog.RESULT_CODE)){
                DateSelectBSDialog.RESULT_OK -> {
                    val dateString = bundle.getString(DateSelectBSDialog.RESULT_DATE)
                    binding.calendarAddDate.setText(dateString.toString())
                }
            }
        }
    }
}