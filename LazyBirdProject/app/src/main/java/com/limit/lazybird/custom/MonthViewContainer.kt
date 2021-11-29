package com.limit.lazybird.custom

import android.view.View
import com.kizitonwose.calendarview.ui.ViewContainer
import com.limit.lazybird.databinding.CalendarMonthHeaderLayoutBinding

class MonthViewContainer(view: View) : ViewContainer(view) {
    private val binding = CalendarMonthHeaderLayoutBinding.bind(view)
    val headerMonth = binding.headerMonth
    val headerYear = binding.headerYear
    val nextMonthBtn = binding.btnCalendarMonthNext
    val prevMonthBtn = binding.btnCalendarMonthPrev
}