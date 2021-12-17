package com.limit.lazybird.ui.calendar

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentCalendarAddBinding
import com.limit.lazybird.models.Schedule
import com.limit.lazybird.models.retrofit.CalendarInfo
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.ui.custom.dialog.DateSelectBSDialog
import com.limit.lazybird.ui.custom.dialog.TimeSelectBSDialog
import com.limit.lazybird.util.parseDay
import com.limit.lazybird.util.parseMonth
import com.limit.lazybird.util.parseYear
import com.limit.lazybird.viewmodel.CalendarAddViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarAddFragment: Fragment(R.layout.fragment_calendar_add) {

    companion object {
        const val TAG = "CalendarAddFragment"
        const val ADD_TYPE = "type"
        const val TYPE_CUSTOM = "custom"
        const val TYPE_TICKETED = "ticketed"
        const val TICKET_INFO = "ticket_info"
        const val IS_ADD = "ticket_is_add"
    }

    private lateinit var binding: FragmentCalendarAddBinding
    private val viewModel: CalendarAddViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    private var isAdd: Boolean = false
    private lateinit var type:String
    private lateinit var exhbtCd: String
    private lateinit var scheduleInfo: Schedule

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalendarAddBinding.bind(view)

        type = arguments?.getString(ADD_TYPE).toString() // custom or ticketed
        isAdd = requireArguments().getBoolean(IS_ADD)

        if(isAdd){
            // 추가하기 모드 일 때
            if(type == TYPE_TICKETED) {
                val calendarInfo:CalendarInfo = arguments?.getParcelable(TICKET_INFO)!!
                exhbtCd = calendarInfo.exhbt_cd.toString()
                // 전시회 이름 고정
                binding.calendarAddExhibition.setText(calendarInfo.exhbt_nm)
                binding.calendarAddExhibition.isFocusable = false
                // 전시회 장소 고정
                binding.calendarAddPlace.setText(calendarInfo.exhbt_lct)
                binding.calendarAddPlace.isFocusable = false
            }
        } else {
            // 수정하기 모드 일 때
            val schedule:Schedule = arguments?.getParcelable(TICKET_INFO)!!
            exhbtCd = schedule.id.toString()
            binding.calendarAddContext.visibility = View.GONE
            binding.calendarAddOkBtn.text = "수정"
            binding.calendarAddExhibition.setText(schedule.scheduleName)
            binding.calendarAddPlace.setText(schedule.schedulePlace)
            binding.calendarAddDate.setText("${schedule.date.parseYear()}-" +
                    "${String.format("%02d", schedule.date.parseMonth()+1)}-" +
                    "${String.format("%02d", schedule.date.parseDay())}")
            binding.calendarAddTimeStart.setText(schedule.startTime)
            binding.calendarAddTimeEnd.setText(schedule.endTime)

            if(type == TYPE_TICKETED) {
                // 전시회 이름 고정
                binding.calendarAddExhibition.isFocusable = false
                // 전시회 장소 고정
                binding.calendarAddPlace.isFocusable = false
            }
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

        binding.calendarAddOkBtn.setOnClickListener {
            if(isAdd){
                // 추가 버튼
                when(type){
                    TYPE_CUSTOM -> {
                        // CUSTOM 전용
                        if(binding.calendarAddExhibition.text.toString() != ""
                            && binding.calendarAddPlace.text.toString() != ""
                            && binding.calendarAddDate.text.toString() != ""
                            && binding.calendarAddTimeStart.text.toString() != ""
                            && binding.calendarAddTimeEnd.text.toString() != ""
                        ) {
                            viewModel.saveCustomInfo(
                                exhbt_nm = binding.calendarAddExhibition.text.toString(),
                                exhbt_lct = binding.calendarAddPlace.text.toString(),
                                reser_dt = binding.calendarAddDate.text.toString().replace("-", "").trim(),
                                start_time = binding.calendarAddTimeStart.text.toString(),
                                end_time = binding.calendarAddTimeEnd.text.toString()
                            )
                            parentActivity.supportFragmentManager.popBackStack()
                        } else {
                            Toast.makeText(context, "모든 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                        }
                    }
                    TYPE_TICKETED -> {
                        // 예약일정
                        if(binding.calendarAddExhibition.text.toString() != ""
                            && binding.calendarAddPlace.text.toString() != ""
                            && binding.calendarAddDate.text.toString() != ""
                            && binding.calendarAddTimeStart.text.toString() != ""
                            && binding.calendarAddTimeEnd.text.toString() != ""
                        ) {
                            viewModel.saveCalendarInfo(
                                exhbt_cd = exhbtCd,
                                reser_dt = binding.calendarAddDate.text.toString().replace("-", "").trim(),
                                start_time = binding.calendarAddTimeStart.text.toString(),
                                end_time = binding.calendarAddTimeEnd.text.toString()
                            )
                            parentActivity.supportFragmentManager.popBackStack()
                        } else {
                            Toast.makeText(context, "모든 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                // 수정 버튼
                when(type){
                    TYPE_CUSTOM -> {
                        // CUSTOM 전용
                        if(binding.calendarAddExhibition.text.toString() != ""
                            && binding.calendarAddPlace.text.toString() != ""
                            && binding.calendarAddDate.text.toString() != ""
                            && binding.calendarAddTimeStart.text.toString() != ""
                            && binding.calendarAddTimeEnd.text.toString() != ""
                        ) {
                            viewModel.updateCustomInfo(
                                exhbt_cd = exhbtCd,
                                exhbt_nm = binding.calendarAddExhibition.text.toString(),
                                exhbt_lct = binding.calendarAddPlace.text.toString(),
                                reser_dt = binding.calendarAddDate.text.toString().replace("-", "").trim(),
                                start_time = binding.calendarAddTimeStart.text.toString(),
                                end_time = binding.calendarAddTimeEnd.text.toString()
                            )
                            parentActivity.supportFragmentManager.popBackStack()
                        } else {
                            Toast.makeText(context, "모든 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                        }
                    }
                    TYPE_TICKETED -> {
                        // 예약일정
                        if(binding.calendarAddExhibition.text.toString() != ""
                            && binding.calendarAddPlace.text.toString() != ""
                            && binding.calendarAddDate.text.toString() != ""
                            && binding.calendarAddTimeStart.text.toString() != ""
                            && binding.calendarAddTimeEnd.text.toString() != ""
                        ) {
                            viewModel.updateCalendarInfo(
                                exhbt_cd = exhbtCd,
                                reser_dt = binding.calendarAddDate.text.toString().replace("-", "").trim(),
                                start_time = binding.calendarAddTimeStart.text.toString(),
                                end_time = binding.calendarAddTimeEnd.text.toString()
                            )
                            parentActivity.supportFragmentManager.popBackStack()
                        } else {
                            Toast.makeText(context, "모든 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                        }
                    }
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
            when(bundle.getString(TimeSelectBSDialog.RESULT_CODE)){
                TimeSelectBSDialog.RESULT_OK -> {
                    val timeString = bundle.getString(TimeSelectBSDialog.RESULT_CONTEXT)
                    binding.calendarAddTimeStart.setText(timeString.toString())
                }
            }
        }
        setFragmentResultListener(TimeSelectBSDialog.TYPE_END) { _, bundle ->
            // 시작 시간 선택 Dialog 결과 확인
            when(bundle.getString(TimeSelectBSDialog.RESULT_CODE)){
                TimeSelectBSDialog.RESULT_OK -> {
                    val timeString = bundle.getString(TimeSelectBSDialog.RESULT_CONTEXT)
                    binding.calendarAddTimeEnd.setText(timeString.toString())
                }
            }
        }
    }
}