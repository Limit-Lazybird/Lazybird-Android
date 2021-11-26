package com.xemic.lazybird.custom

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.kizitonwose.calendarview.ui.ViewContainer
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.CalendarMonthHeaderLayoutBinding

class MonthViewContainer(view: View) : ViewContainer(view) {
    private val binding = CalendarMonthHeaderLayoutBinding.bind(view)
    val headerMonth = binding.headerMonth
    val headerYear = binding.headerYear
    val nextMonthBtn = binding.btnCalendarMonthNext
    val prevMonthBtn = binding.btnCalendarMonthPrev
}