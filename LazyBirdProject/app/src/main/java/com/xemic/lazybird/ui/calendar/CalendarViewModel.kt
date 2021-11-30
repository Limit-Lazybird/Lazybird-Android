package com.xemic.lazybird.ui.calendar

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kizitonwose.calendarview.model.CalendarDay
import com.xemic.lazybird.custom.DayViewContainer
import com.xemic.lazybird.models.Schedule
import com.xemic.lazybird.util.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.DayOfWeek
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

    private val _isCalendarExpanded = MutableLiveData(true)
    val isCalendarExpanded:LiveData<Boolean> get() = _isCalendarExpanded

    var selectedDayViewContainer: DayViewContainer? = null // 선택된 날짜의 DayViewContainer
    val firstDayOfWeek: DayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var scheduleList = listOf<Schedule>()
    var scheduleListDict: Map<Long, MutableList<Schedule>> = mapOf()

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
                    "2021-11-28".toDate(),
                    "라면먹어야하는 날",
                    "우리집",
                    "20:00",
                    "21:00",
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
        scheduleListDict = mutableMapOf<Long, MutableList<Schedule>>().also{
            for(schedule in scheduleList) {
                if(!it.containsKey(schedule.date.time))
                    it[schedule.date.time] = mutableListOf(schedule)
                else
                    it[schedule.date.time]!!.add(schedule)
            }
        }.toMap()
    }

    fun getSchedule(date:Date) = scheduleListDict[date.time]!!.toList()

    fun getScheduleCount(date:Date): Int =
        if(scheduleListDict.containsKey(date.time)){
            scheduleListDict[date.time]!!.size
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

    fun calendarExpandedUpdate(isExpanded: Boolean){
        _isCalendarExpanded.postValue(isExpanded)
    }
}