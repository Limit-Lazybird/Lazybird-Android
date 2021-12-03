package com.limit.lazybird.ui.calendar

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.kizitonwose.calendarview.model.CalendarDay
import com.limit.lazybird.custom.DayViewContainer
import com.limit.lazybird.models.Schedule
import com.limit.lazybird.models.retrofit.CalendarInfo
import com.limit.lazybird.util.toDate
import com.limit.lazybird.util.toDate3
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.temporal.WeekFields
import java.util.*
import javax.inject.Inject

/**************** CalendarAddViewModel ******************
 * 메인화면(캘린더 탭) (ViewModel)
 * 캘린더에서 (예약된 or 예약되지 않은)전시일정정보 확인
 * Todo : 서버에서 API 받아오기
 ********************************************** ***/
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: CalendarRepository
) : ViewModel() {

    companion object {
        const val TAG = "CalendarViewModel"
    }

    private val today = Calendar.getInstance().time

    // 선택된 날짜 (default: 오늘)
    private val _selectedDateLiveData = MutableLiveData(today)
    val selectedDateLiveData: Flow<Date> = _selectedDateLiveData.asFlow()

    private val _unregisteredListLiveData = MutableLiveData<List<CalendarInfo>>()
    val unregisteredListLiveData:LiveData<List<CalendarInfo>> get() = _unregisteredListLiveData

    var selectedDayViewContainer: DayViewContainer? = null // 선택된 날짜의 DayViewContainer
    val firstDayOfWeek: DayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

    private val _registeredListLiveData = MutableLiveData<List<CalendarInfo>>()
    val registeredListLiveData:LiveData<List<CalendarInfo>> get() =  _registeredListLiveData
    val scheduleListDict: LiveData<Map<Long, MutableList<Schedule>>> get() =
        registeredListLiveData.map { calendarInfoList ->
            val scheduleList = calendarInfoList.map { calendarInfo ->
                Schedule(
                    date = calendarInfo.reser_dt.toDate3(),
                    scheduleName = calendarInfo.exhbt_nm,
                    schedulePlace = calendarInfo.exhbt_lct,
                    startTime = calendarInfo.start_time,
                    endTime = calendarInfo.end_time,
                    isVisited = calendarInfo.visit_yn == "Y"
                )
            }
            mutableMapOf<Long, MutableList<Schedule>>().also{
                for(schedule in scheduleList) {
                    if(!it.containsKey(schedule.date.time))
                        it[schedule.date.time] = mutableListOf(schedule)
                    else
                        it[schedule.date.time]!!.add(schedule)
                }
            }.toMap()
        }

    private lateinit var token: String

    init {
        initToken()
        initRegisteredList()
        initUnregisteredList()
    }

    private fun initToken() = viewModelScope.launch {
        token = repository.getPreferenceFlow().first()
    }

    private fun initRegisteredList() = viewModelScope.launch {
        // 예약된 전시리스트 받기
        repository.getRegistList(token).let { response ->
            if (response.body() != null) {
                _registeredListLiveData.postValue(response.body()!!.calList)
            } else {
                Log.e(TAG, "response.body() is null")
            }
        }
    }

    private fun initUnregisteredList() = viewModelScope.launch {
        // 전시리스트 받기
        repository.getUnRegistList(token).let { response ->
            if (response.body() != null) {
                _unregisteredListLiveData.postValue(response.body()!!.calList)
            } else {
                Log.e(TAG, "response.body() is null")
            }
        }
    }

    fun getSchedule(date:Date) = scheduleListDict.value!![date.time]!!.toList()

    fun getScheduleCount(date:Date): Int =
        if(scheduleListDict.value != null && scheduleListDict.value!!.containsKey(date.time)){
            scheduleListDict.value!![date.time]!!.size
        } else {
            0
        }

    fun selectDay(container: DayViewContainer, day: CalendarDay) {
        selectedDayViewContainer?.isSelected?.visibility = View.INVISIBLE // 기존 viewContainer 선택 표시 제거
        selectedDayViewContainer = container // 선택한 viewContainer 로 저장
        container.isSelected.visibility = View.VISIBLE // viewContainer 선택 표시 생성
        _selectedDateLiveData.postValue( // 선택된 날짜 업데이트
            day.toDate()
        )
    }

    fun getUnregisteredInfo(position: Int) = unregisteredListLiveData.value!![position]
}