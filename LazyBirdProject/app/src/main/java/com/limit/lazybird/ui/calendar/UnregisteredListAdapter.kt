package com.limit.lazybird.ui.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.limit.lazybird.R
import com.limit.lazybird.databinding.ItemUnregisteredBinding
import com.limit.lazybird.models.UnregisteredItem

/**************** UnregisteredListAdapter ******************
 * 메인화면(캘린더 탭) >> 추가되지 않은 전시일정 목록화면 (Recycler Adapter)
 * 추가되지 않은 전시일정 몱록 확인하는 목록
 ********************************************** ***/
class UnregisteredListAdapter(
    private val items: List<UnregisteredItem>
): RecyclerView.Adapter<UnregisteredListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(holder: UnregisteredListAdapter.ViewHolder, view: View, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null


    private lateinit var binding: ItemUnregisteredBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val exhibitionName = binding.itemUnregisteredName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnregisteredListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_unregistered, parent, false)
        binding = ItemUnregisteredBinding.bind(view)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            exhibitionName.text = items[position].exhibitionName
            exhibitionName.setOnClickListener {
                itemClickListener?.onItemClick(holder, it, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}