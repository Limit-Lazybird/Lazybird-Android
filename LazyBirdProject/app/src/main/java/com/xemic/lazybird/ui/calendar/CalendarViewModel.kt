package com.xemic.lazybird.ui.calendar

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kizitonwose.calendarview.model.CalendarDay
import com.xemic.lazybird.custom.DayViewContainer
import com.xemic.lazybird.models.Schedule
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: CalendarRepository
) : ViewModel() {
    private val today = LocalDate.now()

    // 선택된 날짜 (default: 오늘)
    private val _selectedLocalDateLiveData = MutableLiveData(today)
    var selectedLocalDateLiveData: LiveData<LocalDate> = _selectedLocalDateLiveData

    var selectedDayViewContainer: DayViewContainer? = null // 선택된 날짜의 DayViewContainer
    val firstDayOfWeek: DayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var currentMonth = MutableLiveData(YearMonth.now()) // 현재 달
    var scheduleList = listOf<Schedule>()
    var scheduleListDict: Map<LocalDate, MutableList<Schedule>> = mapOf()

    init {
        getDummyData()
    }

    private fun getDummyData() {
        scheduleList =
            listOf(
                Schedule(LocalDate.now(), "즐거운 재미있는 제믹 졸업전시회"),
                Schedule(LocalDate.now(), "So happy coding xemic graduate exhibition")
            )
        scheduleListDict = mutableMapOf<LocalDate, MutableList<Schedule>>().also{
            for(schedule in scheduleList) {
                if(!it.containsKey(schedule.date))
                    it[schedule.date] = mutableListOf(schedule)
                else
                    it[schedule.date]!!.add(schedule)
            }
        }.toMap()
    }

    fun selectDay(container: DayViewContainer, day: CalendarDay) {
        selectedDayViewContainer?.isToday?.visibility = View.INVISIBLE // 기존 viewContainer 선택 표시 제거
        selectedDayViewContainer = container // 선택한 viewContainer 로 저장
        container.isToday.visibility = View.VISIBLE // viewContainer 선택 표시 생성
        _selectedLocalDateLiveData.value = day.date // 선택된 날짜 업데이트
    }

    fun clickNextMonthBtn() {
        currentMonth.value = currentMonth.value!!.plusMonths(1)
    }

    fun clickPrevMonthBtn() {
        currentMonth.value = currentMonth.value!!.minusMonths(1)
    }

}