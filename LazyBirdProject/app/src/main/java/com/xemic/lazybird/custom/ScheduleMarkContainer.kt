package com.xemic.lazybird.custom

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.ItemCalendarDayMarkBinding

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