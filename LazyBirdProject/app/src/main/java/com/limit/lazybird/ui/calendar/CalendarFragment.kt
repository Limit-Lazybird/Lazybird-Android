package com.limit.lazybird.ui.calendar

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.kizitonwose.calendarview.model.*
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.utils.Size
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import com.limit.lazybird.R
import com.limit.lazybird.custom.DayViewContainer
import com.limit.lazybird.custom.MonthViewContainer
import com.limit.lazybird.custom.ScheduleMarkContainer
import com.limit.lazybird.databinding.FragmentCalendarBinding
import com.limit.lazybird.models.CalendarInfoList
import com.limit.lazybird.models.UnregisteredItem
import com.limit.lazybird.models.retrofit.CalendarInfo
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.ui.calendaradd.CalendarAddFragment
import com.limit.lazybird.ui.exhibition.ExhibitionRefreshBSDialog
import com.limit.lazybird.ui.onboarding.OnbFragment
import com.limit.lazybird.util.*
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.*

/**************** CalendarFragment ******************
 * 메인화면(캘린더 탭) (Fragment)
 * 캘린더에서 (예약된 or 예약되지 않은)전시일정정보 확인
 ********************************************** ***/
@AndroidEntryPoint
class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    companion object {
        const val TAG = "CalendarFragment"
    }

    private val DAY_VIEW_HEGIHT = 120

    private lateinit var binding: FragmentCalendarBinding
    private val viewModel: CalendarViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    lateinit var unregisteredList: CalendarInfoList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalendarBinding.bind(view)
        calendarViewInit()
        viewModel.selectedDateLiveData.observe(viewLifecycleOwner) { selectedDate ->
            scheduleLayoutUpdate(selectedDate)
        }
        viewModel.unregisteredListLiveData.observe(viewLifecycleOwner) { calendarInfoList ->
            unregisteredList = CalendarInfoList(calendarInfoList)
            binding.calendarNotAddedCount.text = calendarInfoList.size.toString()
        }
        binding.calendarCustomBtn.setOnClickListener {
            // 커스텀 일정 추가하기 버튼
            moveToCalendarAdd()
        }
        binding.calendarSubHeader.setOnClickListener {
            // 추가되지 않은 전시 일정이 N개 있습니다 버튼
            val bundle = Bundle().apply {
                putParcelable(UnregisteredListBSDialog.EXHIBITION_LIST, unregisteredList)
            }
            UnregisteredListBSDialog().apply {
                arguments = bundle
            }.show(
                parentFragmentManager,
                UnregisteredListBSDialog.TAG
            )
        }

        setFragmentResultListener(UnregisteredListBSDialog.TAG) { _, bundle ->
            // 전시성향분석 재설정 Dialog 선택 결과 확인
            when(bundle.getString(UnregisteredListBSDialog.RESULT_CODE)){
                UnregisteredListBSDialog.RESULT_OK -> {
                    val position = bundle.getInt(UnregisteredListBSDialog.SELECTED_POSITION)
                    moveToCalendarAdd(viewModel.getUnregisteredInfo(position))
                }
            }
        }
    }

    private fun moveToCalendarAdd() {
        parentActivity.supportFragmentManager.replaceFragment(CalendarAddFragment().apply {
            arguments = bundleOf(CalendarAddFragment.ADD_TYPE to CalendarAddFragment.TYPE_CUSTOM)
        })
    }

    private fun moveToCalendarAdd(calendarInfo: CalendarInfo) {
        parentActivity.supportFragmentManager.replaceFragment(CalendarAddFragment().apply {
            arguments = bundleOf(
                CalendarAddFragment.ADD_TYPE to CalendarAddFragment.TYPE_TICKETED,
                CalendarAddFragment.TICKET_INFO to calendarInfo
            )
        })
    }

    private fun scheduleLayoutUpdate(selectedDay: Date) {
        // selectedDay 에 해당하는 일정내용을 업데이트
        val scheduleListDict = viewModel.scheduleListDict
        if (scheduleListDict.containsKey(selectedDay.time) && scheduleListDict[selectedDay.time]!!.isNotEmpty()) {
            // schedule 이 존재하고, 일정이 1개 이상
            binding.calendarScheduleRecyclerView.visibility = View.VISIBLE
            binding.calendarScheduleRecyclerView.adapter =
                CalendarScheduleAdapter(scheduleListDict[selectedDay.time]!!)
        } else {
            // schedule 이 존재하지 않음
            binding.calendarScheduleRecyclerView.visibility = View.GONE
        }
    }

    private fun calendarViewInit() {
        // 세로 크기 조절
        binding.calendarView.daySize = Size(
            binding.calendarView.daySize.width,
            DAY_VIEW_HEGIHT
        )
        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View): DayViewContainer = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                if (day.owner == DayOwner.THIS_MONTH) {
                    // 이번달
                    // 오늘 날짜 인 경우
                    if(day.date == LocalDate.now()){
                        container.isToday.visibility = View.VISIBLE // 오늘인 경우
                    } else {
                        container.isToday.visibility = View.INVISIBLE // 오늘이 아닌 경우
                    }

                    // 선택된 날짜 인 경우
                    if (day.toDate() == viewModel.selectedDateLiveData.value) {
                        container.isSelected.visibility = View.VISIBLE // 선택 표시 생성
                        viewModel.selectedDayViewContainer = container // container 업데이트
                    }

                    // 요일별 구분
                    when (day.date.dayOfWeek) {
                        DayOfWeek.SATURDAY ->
                            container.textView.setTextColor(Color.WHITE) // 토요일
                        DayOfWeek.SUNDAY ->
                            container.textView.setTextColor(Color.WHITE) // 일요일
                        else ->
                            container.textView.setTextColor(Color.WHITE) // 평일
                    }
                } else {
                    container.textView.setTextColor(Color.TRANSPARENT) // 이전달, 다음달 글씨 색
                }
                container.textView.text = day.date.dayOfMonth.toString()
                container.textView.setOnClickListener {
                    viewModel.selectDay(container, day) // 날짜를 클릭 했을 때
                }

                container.scheduleMark.removeAllViews()
                if (day.owner == DayOwner.THIS_MONTH &&
                    viewModel.getScheduleCount(day.toDate()) > 0) {
                    for (schedule in viewModel.getSchedule(day.toDate())) {
                        container.scheduleMark.addView(
                            ScheduleMarkContainer(container.view.context, null).apply {
                                if (schedule.isVisited)
                                    setColor(resources.getColorStateList(R.color.green, null))
                                else
                                    setColor(resources.getColorStateList(R.color.gray04, null))
                            }
                        )
                    }
                }
            }
        }

        binding.calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View): MonthViewContainer = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                    container.headerMonth.text = "${month.month}월"
                    container.headerYear.text = "${month.year}"
                    container.nextMonthBtn.setOnClickListener {
                        // 다음 달 버튼 클릭
                        binding.calendarView.findFirstVisibleMonth()?.let {
                            binding.calendarView.smoothScrollToMonth(it.yearMonth.next)
                        }
                    }
                    container.prevMonthBtn.setOnClickListener {
                        // 이전 달 버튼 클릭
                        binding.calendarView.findFirstVisibleMonth()?.let {
                            binding.calendarView.smoothScrollToMonth(it.yearMonth.previous)
                        }
                    }
                }
            }

        // Todo : why error occured at here... TT
//        binding.calendarHeader.setOnClickListener {
//            if (tmp) {
//                binding.calendarView.updateMonthConfiguration(
//                    InDateStyle.ALL_MONTHS,
//                    OutDateStyle.NONE,
//                    1,
//                    false
//                )
//            } else {
//                binding.calendarView.updateMonthConfiguration(
//                    InDateStyle.FIRST_MONTH,
//                    OutDateStyle.NONE,
//                    6,
//                    true
//                )
//            }
//            tmp = !tmp
//        }

        val currentMonth = YearMonth.now()
        binding.calendarView.setup(
            currentMonth.minusMonths(10),
            currentMonth.plusMonths(10),
            viewModel.firstDayOfWeek
        )
        binding.calendarView.scrollToMonth(currentMonth)
    }
}