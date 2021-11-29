package com.limit.lazybird.ui.calendar

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kizitonwose.calendarview.model.CalendarDay
import com.limit.lazybird.custom.DayViewContainer
import com.limit.lazybird.models.Schedule
import com.limit.lazybird.util.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: CalendarRepository
) : ViewModel() {
    private val today = Calendar.getInstance().time

    // 선택된 날짜 (default: 오늘)
    private val _selectedDateLiveData = MutableLiveData(today)
    var selectedDateLiveData: LiveData<Date> = _selectedDateLiveData

    var selectedDayViewContainer: DayViewContainer? = null // 선택된 날짜의 DayViewContainer
    val firstDayOfWeek: DayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var currentMonth = MutableLiveData(YearMonth.now()) // 현재 달
    var scheduleList = listOf<Schedule>()
    var scheduleListDict: Map<Date, MutableList<Schedule>> = mapOf()

    init {
        getDummyData()
    }

    private fun getDummyData() {
        scheduleList =
            listOf(
                Schedule(
                    "2021-11-28".toDate(),
                    "요시고 사진전",
                    "그라운드 시소 서촌",
                    "11:00",
                    "12:00",
                    true
                ),
                Schedule(
                    "2021-11-28".toDate(),
                    "So happy coding xemic graduate exhibition",
                    "프론트원",
                    "15:00",
                    "16:00",
                    false
                ),
                Schedule(
                    "2021-11-30".toDate(),
                    "나 밥먹으러 가는 날",
                    "건국대학교",
                    "15:00",
                    "16:00",
                    false
                ),
            )
        scheduleListDict = mutableMapOf<Date, MutableList<Schedule>>().also{
            for(schedule in scheduleList) {
                if(!it.containsKey(schedule.date))
                    it[schedule.date] = mutableListOf(schedule)
                else
                    it[schedule.date]!!.add(schedule)
            }
        }.toMap()
    }

    fun getScheduleCount(date:Date): Int =
        if(scheduleListDict.containsKey(date)){
            scheduleListDict[date]!!.size
        } else {
            0
        }

    fun selectDay(container: DayViewContainer, day: CalendarDay) {
        selectedDayViewContainer?.isToday?.visibility = View.INVISIBLE // 기존 viewContainer 선택 표시 제거
        selectedDayViewContainer = container // 선택한 viewContainer 로 저장
        container.isToday.visibility = View.VISIBLE // viewContainer 선택 표시 생성
        _selectedDateLiveData.postValue( // 선택된 날짜 업데이트
            day.toDate()
        )
    }

    fun clickNextMonthBtn() {
        currentMonth.value = currentMonth.value!!.plusMonths(1)
    }

    fun clickPrevMonthBtn() {
        currentMonth.value = currentMonth.value!!.minusMonths(1)
    }

}