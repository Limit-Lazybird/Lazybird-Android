package com.limit.lazybird.ui.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.limit.lazybird.databinding.ItemDayScheduleBinding
import com.limit.lazybird.models.DaySchedule
import java.time.LocalDate

class CalendarDayScheduleAdapter(
    private val items: List<DaySchedule>
) : RecyclerView.Adapter<CalendarDayScheduleAdapter.ViewHolder>() {

    private lateinit var binding: ItemDayScheduleBinding
    private val today = LocalDate.now()
    private val DAYOFWEEK = listOf("월", "화", "수", "목", "금", "토", "일")

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val scheduleDay = binding.dayScheduleDay
        val isToday = binding.dayScheduleIsToday
        val scheduleDayOfWeek = binding.dayScheduleDayOfWeek
        val scheduleCount = binding.dayScheduleCount
        val scheduleList = binding.dayScheduleItems
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemDayScheduleBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            scheduleDay.text = items[position].scheduleDate.dayOfMonth.toString()
            if (today == items[position].scheduleDate)
                isToday.visibility = View.VISIBLE
            scheduleDayOfWeek.text = DAYOFWEEK[items[position].scheduleDate.dayOfWeek.value]
            scheduleCount.text = "${items[position].schedules.size}개의 일정이 있습니다."
            scheduleList.adapter // Todo: Adapter 작성
        }
    }

    override fun getItemCount(): Int = items.size
}