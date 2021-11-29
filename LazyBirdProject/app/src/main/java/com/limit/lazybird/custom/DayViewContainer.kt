package com.limit.lazybird.custom

import android.view.View
import com.kizitonwose.calendarview.ui.ViewContainer
import com.limit.lazybird.databinding.CalendarDayLayoutBinding

class DayViewContainer(view: View) : ViewContainer(view) {
    private val binding = CalendarDayLayoutBinding.bind(view)
    val textView = binding.calendarDayText
    val isToday = binding.calenadrDayIsToday
    val scheduleMark = binding.calendarDayScheduleMarks
    // Todo : 여기서 setOnClickListener 를 해야하는데... 이 경우 CalendarViewModel 로 보내도록 시도해보기
}