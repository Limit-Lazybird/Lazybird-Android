package com.limit.lazybird.custom

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.limit.lazybird.R
import com.limit.lazybird.databinding.ItemCalendarDayMarkBinding

/*************** ScheduleMarkContainer ******************
 * CalendarFragment 화면에서 사용하는 Calendar 의 일정이 있는지 표시하는 점을 담당하는 Container
 ********************************************** ***/
class ScheduleMarkContainer(context: Context, attrs: AttributeSet?): RelativeLayout(context, attrs) {
    companion object{
        private lateinit var binding: ItemCalendarDayMarkBinding
    }

    init {
        val view = inflate(context, R.layout.item_calendar_day_mark, this)
        binding = ItemCalendarDayMarkBinding.bind(view)
    }

    fun setColor(tint: ColorStateList) {
        binding.itemMark.backgroundTintList = tint
    }
}