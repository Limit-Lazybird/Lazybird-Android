package com.limit.lazybird.ui.custom

import android.view.View
import com.kizitonwose.calendarview.ui.ViewContainer
import com.limit.lazybird.databinding.CalendarMonthHeaderLayoutBinding

/*************** MonthViewContainer ******************
 * CalendarFragment 화면에서 사용하는 Calendar 의 Month 만을 담당하는 Container
 ********************************************** ***/
class MonthViewContainer(view: View) : ViewContainer(view) {
    private val binding = CalendarMonthHeaderLayoutBinding.bind(view)
    val headerMonth = binding.headerMonth
    val headerYear = binding.headerYear
    val nextMonthBtn = binding.btnCalendarMonthNext
    val prevMonthBtn = binding.btnCalendarMonthPrev
}