package com.limit.lazybird.ui.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.limit.lazybird.R
import com.limit.lazybird.databinding.ItemScheduleBinding
import com.limit.lazybird.models.Schedule
import com.limit.lazybird.util.parseDay
import com.limit.lazybird.util.parseDayOfWeek

/**************** CalendarScheduleAdapter ******************
 * 메인화면(캘린더 탭) (Recycler Adapter)
 * 캘린더에서 (예약된 or 예약되지 않은)전시일정정보 확인
 ********************************************** ***/
class CalendarScheduleAdapter(
    private val items: List<Schedule>
):RecyclerView.Adapter<CalendarScheduleAdapter.ViewHolder>() {

    private val DAY_OF_WEEK = arrayOf("월", "화", "수", "목", "금", "토", "일")
    private lateinit var binding: ItemScheduleBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val scheduleDayofweek = binding.itemScheduleDayofweek
        val scheduleDay = binding.itemScheduleDay
        val scheduleMark = binding.itemScheduleMark
        val scheduleName = binding.itemScheduleName
        val scheduleTime = binding.itemScheduleTime
        val schedulePlace = binding.itemSchedulePlace
        val isVisited = binding.itemScheduleIsVisited
        val isVisitedIcon = binding.itemScheduleIsVisitedImg
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemScheduleBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            scheduleDayofweek.text = DAY_OF_WEEK[items[position].date.parseDayOfWeek()-1]
            scheduleDay.text = items[position].date.parseDay().toString()
            scheduleName.text = items[position].scheduleName
            scheduleTime.text = "${items[position].startTime} - ${items[position].endTime}"
            schedulePlace.text = items[position].schedulePlace
            
            // 첫번째 일정인가?
            if(position == 0){
                scheduleDayofweek.visibility = View.VISIBLE
                scheduleDay.visibility = View.VISIBLE
            } else {
                scheduleDayofweek.visibility = View.INVISIBLE
                scheduleDay.visibility = View.INVISIBLE
            }

            // 방문한 일정인가?ㄴ
            if(items[position].isVisited){
                isVisited.setTextColor(holder.itemView.resources.getColor(R.color.or01, null))
                isVisitedIcon.setColorFilter(holder.itemView.resources.getColor(R.color.or01, null))
                scheduleMark.setBackgroundColor(holder.itemView.resources.getColor(R.color.green, null))
            } else {
                isVisited.setTextColor(holder.itemView.resources.getColor(R.color.gray_600, null))
                isVisitedIcon.setColorFilter(holder.itemView.resources.getColor(R.color.gray_600, null))
                scheduleMark.setBackgroundColor(holder.itemView.resources.getColor(R.color.white, null))
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}