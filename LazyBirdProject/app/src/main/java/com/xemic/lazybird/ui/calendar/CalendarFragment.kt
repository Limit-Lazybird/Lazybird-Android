package com.xemic.lazybird.ui.calendar

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendarview.model.*
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.MonthScrollListener
import com.kizitonwose.calendarview.utils.Size
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import com.xemic.lazybird.R
import com.xemic.lazybird.custom.DayViewContainer
import com.xemic.lazybird.custom.MonthViewContainer
import com.xemic.lazybird.custom.ScheduleMarkContainer
import com.xemic.lazybird.databinding.FragmentCalendarBinding
import com.xemic.lazybird.util.*
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.*

@AndroidEntryPoint
class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private val DAY_VIEW_HEGIHT = 120

    private lateinit var binding: FragmentCalendarBinding
    private val DAY_OF_WEEK = listOf("월", "화", "수", "목", "금", "토", "일")
    private val calendarViewModel: CalendarViewModel by viewModels()

    private var tmp = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalendarBinding.bind(view)
        calendarViewInit()
        calendarViewModel.selectedDateLiveData.observe(viewLifecycleOwner) { selectedDate ->
            scheduleLayoutUpdate(selectedDate)
        }
    }

    private fun scheduleLayoutUpdate(selectedDay: Date) {
        // selectedDay 에 해당하는 일정내용을 업데이트
        val scheduleListDict = calendarViewModel.scheduleListDict
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
                    if (day.toDate() == calendarViewModel.selectedDateLiveData.value) {
                        // 오늘이 선택된 날짜 인 경우
                        container.isToday.visibility = View.VISIBLE // 선택 표시 생성
                        calendarViewModel.selectedDayViewContainer = container // container 업데이트
                    }
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
                    calendarViewModel.selectDay(container, day) // 날짜를 클릭 했을 때
                }

                container.scheduleMark.removeAllViews()
                if (day.owner == DayOwner.THIS_MONTH &&
                    calendarViewModel.getScheduleCount(day.toDate()) > 0) {
                    for (schedule in calendarViewModel.getSchedule(day.toDate())) {
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

        binding.calendarHeader.setOnClickListener {
            if (tmp) {
                binding.calendarView.updateMonthConfiguration(
                    InDateStyle.ALL_MONTHS,
                    OutDateStyle.NONE,
                    1,
                    false
                )
            } else {
                binding.calendarView.updateMonthConfiguration(
                    InDateStyle.FIRST_MONTH,
                    OutDateStyle.NONE,
                    6,
                    true
                )
            }
            tmp = !tmp
        }

        val currentMonth = YearMonth.now()
        binding.calendarView.setup(
            currentMonth.minusMonths(10),
            currentMonth.plusMonths(10),
            calendarViewModel.firstDayOfWeek
        )
        binding.calendarView.scrollToMonth(currentMonth)
    }
}