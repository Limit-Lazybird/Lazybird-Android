package com.xemic.lazybird.ui.calendar

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.xemic.lazybird.R
import com.xemic.lazybird.custom.DayViewContainer
import com.xemic.lazybird.custom.MonthViewContainer
import com.xemic.lazybird.custom.ScheduleMarkContainer
import com.xemic.lazybird.databinding.FragmentCalendarBinding
import com.xemic.lazybird.util.parseDayOfWeek
import com.xemic.lazybird.util.parseMonth
import com.xemic.lazybird.util.toDate
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.util.*

@AndroidEntryPoint
class CalendarFragment : Fragment(R.layout.fragment_calendar) {
    private lateinit var binding: FragmentCalendarBinding
    private val DAY_OF_WEEK = listOf("월", "화", "수", "목", "금", "토", "일")
    private val calendarViewModel: CalendarViewModel by viewModels()

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
        if (scheduleListDict.containsKey(selectedDay) && scheduleListDict[selectedDay]!!.isNotEmpty()) {
            // schedule 이 존재하고, 일정이 1개 이상
            binding.calendarScheduleLayout.visibility = View.VISIBLE // layout 보여주기
            binding.calendarScheduleDay.text = selectedDay.parseMonth().toString()
            if (selectedDay == calendarViewModel.selectedDateLiveData.value)
                binding.calendarScheduleIsToday.visibility = View.VISIBLE
            else
                binding.calendarScheduleIsToday.visibility = View.INVISIBLE
            binding.calendarScheduleDayOfWeek.text = DAY_OF_WEEK[selectedDay.parseDayOfWeek()-1]
            binding.calendarScheduleCount.text = "${scheduleListDict[selectedDay]!!.size}개의 일정이 있습니다."
            binding.calendarScheduleItems.adapter =
                CalendarScheduleAdapter(scheduleListDict[selectedDay]!!)
        } else {
            // schedule 이 존재하지 않음
            binding.calendarScheduleLayout.visibility = View.INVISIBLE
        }
    }

    private fun calendarViewInit() {
        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                if (day.owner == DayOwner.THIS_MONTH) {
                    // 이번달
                    if(day.toDate() == calendarViewModel.selectedDateLiveData.value){
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

                repeat(calendarViewModel.getScheduleCount(day.toDate())){
                    container.scheduleMark.addView(
                        ScheduleMarkContainer(container.view.context, null)
                    )
                }
            }

            override fun create(view: View): DayViewContainer = DayViewContainer(view)

        }

        binding.calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                container.headerMonth.text = "${month.month}월"
                container.headerYear.text = "${month.year}"
                container.nextMonthBtn.setOnClickListener {
                    calendarViewModel.clickNextMonthBtn() // 다음 달 버튼 클릭
                }
                container.prevMonthBtn.setOnClickListener {
                    calendarViewModel.clickPrevMonthBtn() // 이전 달 버튼 클릭
                }
            }

            override fun create(view: View): MonthViewContainer = MonthViewContainer(view)

        }

        calendarViewModel.currentMonth.observe(viewLifecycleOwner) { currentMonth ->
            binding.calendarView.setup(currentMonth, currentMonth, calendarViewModel.firstDayOfWeek)
        }
    }
}