package com.limit.lazybird.ui.calendaradd

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentCalendarAddBinding
import com.limit.lazybird.models.retrofit.CalendarInfo
import com.limit.lazybird.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarAddFragment: Fragment(R.layout.fragment_calendar_add) {

    companion object {
        const val TAG = "CalendarAddFragment"
        const val ADD_TYPE = "type"
        const val TYPE_CUSTOM = "custom"
        const val TYPE_TICKETED = "ticketed"
        const val TICKET_INFO = "ticket_info"
    }

    private lateinit var binding: FragmentCalendarAddBinding
    private val viewModel: CalendarAddViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    private var type = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalendarAddBinding.bind(view)

        type = arguments?.getString(ADD_TYPE).toString() // custom or ticketed
        if(type == TYPE_TICKETED) {
            val calendarInfo = arguments?.getParcelable<CalendarInfo>(TICKET_INFO)!!
            binding.calendarAddExhibition.setText(calendarInfo.exhbt_nm)
            binding.calendarAddExhibition.isFocusable = false
            binding.calendarAddPlace.setText(calendarInfo.exhbt_lct)
            binding.calendarAddPlace.isFocusable = false
        }

        binding.calendarAddBackBtn.setOnClickListener {
            parentActivity.supportFragmentManager.popBackStack()
        }

        binding.calendarAddDate.setOnClickListener { 
            // 날짜 버튼 클릭
            DateSelectBSDialog().show(
                parentFragmentManager,
                DateSelectBSDialog.TAG
            )
        }
        
        binding.calendarAddTimeStart.setOnClickListener { 
            // 시작시간 버튼 클릭
            TimeSelectBSDialog().apply {
                arguments = bundleOf(TimeSelectBSDialog.TIME_TYPE to  TimeSelectBSDialog.TYPE_START)
            }.show(
                parentFragmentManager,
                TimeSelectBSDialog.TAG
            )
        }
        
        binding.calendarAddTimeEnd.setOnClickListener { 
            // 끝시간 버튼 클릭
            TimeSelectBSDialog().apply {
                arguments = bundleOf(TimeSelectBSDialog.TIME_TYPE to  TimeSelectBSDialog.TYPE_END)
            }.show(
                parentFragmentManager,
                TimeSelectBSDialog.TAG
            )
        }

        binding.calendarAddDeleteBtn.setOnClickListener {
            // 삭제 버튼
            when(type){
                TYPE_CUSTOM -> {
                    // 뒤로가기
                    parentActivity.supportFragmentManager.popBackStack()
                }
                TYPE_TICKETED -> {
                    // 예약일정 삭제

                }
            }
        }

        binding.calendarAddOkBtn.setOnClickListener {
            // 추가 버튼
            when(type){
                TYPE_CUSTOM -> {
                    // CUSTOM 전용

                }
                TYPE_TICKETED -> {
                    // 예약일정 삭제
                }
            }
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

        setFragmentResultListener(TimeSelectBSDialog.TYPE_START) { _, bundle ->
            // 시작 시간 선택 Dialog 결과 확인
            Log.e("test", "1")
            when(bundle.getString(TimeSelectBSDialog.RESULT_CODE)){
                TimeSelectBSDialog.RESULT_OK -> {
                    val timeString = bundle.getString(TimeSelectBSDialog.RESULT_CONTEXT)
                    binding.calendarAddTimeStart.setText(timeString.toString())
                }
            }
        }
        setFragmentResultListener(TimeSelectBSDialog.TYPE_END) { _, bundle ->
            // 시작 시간 선택 Dialog 결과 확인
            Log.e("test", "2")
            when(bundle.getString(TimeSelectBSDialog.RESULT_CODE)){
                TimeSelectBSDialog.RESULT_OK -> {
                    val timeString = bundle.getString(TimeSelectBSDialog.RESULT_CONTEXT)
                    binding.calendarAddTimeEnd.setText(timeString.toString())
                }
            }
        }
    }
}