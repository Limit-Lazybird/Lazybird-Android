package com.limit.lazybird.ui.custom

import android.view.View
import com.kizitonwose.calendarview.ui.ViewContainer
import com.limit.lazybird.databinding.CalendarDayLayoutBinding

/*************** DayViewContainer ******************
 * CalendarFragment 화면에서 사용하는 Calendar 의 Day 만을 담당하는 Container
 ********************************************** ***/
class DayViewContainer(view: View) : ViewContainer(view) {
    private val binding = CalendarDayLayoutBinding.bind(view)
    val textView = binding.calendarDayText
    val isSelected = binding.calenadrDayIsSelected
    val isToday = binding.calenadrDayIsToday
    val scheduleMark = binding.calendarDayScheduleMarks
}