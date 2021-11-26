package com.xemic.lazybird.ui.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.ItemScheduleBinding
import com.xemic.lazybird.models.Schedule

class CalendarScheduleAdapter(
    private val items: List<Schedule>
):RecyclerView.Adapter<CalendarScheduleAdapter.ViewHolder>() {

    private lateinit var binding: ItemScheduleBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val scheduleMark = binding.scheduleMark
        val scheduleName = binding.scheduleName
        val isVisited = binding.scheduleIsVisited
        val itemView = binding.scheduleDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemScheduleBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            scheduleName.text = items[position].scheduleName
            if(items[position].isVisited){
                scheduleName.setTextColor(Color.YELLOW)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}