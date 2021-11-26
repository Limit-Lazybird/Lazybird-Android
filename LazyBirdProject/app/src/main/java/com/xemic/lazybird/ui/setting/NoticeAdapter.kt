package com.xemic.lazybird.ui.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.ItemNoticeBinding
import com.xemic.lazybird.models.NoticeInfo
import com.xemic.lazybird.util.applyEscapeSequence

class NoticeAdapter(
    val list: List<NoticeInfo>
): RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onExpandBtnClick(holder: NoticeAdapter.ViewHolder, view: View, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    private lateinit var binding: ItemNoticeBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val noticeDate = binding.itemNoticeDate
        val noticeTitle = binding.itemNoticeTitle
        val noticeContext = binding.itemNoticeContext
        val noticeExpandBtn = binding.itemNoticeExpandBtn
        val isExpanded = MutableLiveData(false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_notice, parent, false)
        binding = ItemNoticeBinding.bind(view)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noticeDate.text = list[position].date
        holder.noticeTitle.text = list[position].title
        holder.noticeContext.text = list[position].context.applyEscapeSequence()
        holder.noticeExpandBtn.setOnClickListener {
            itemClickListener?.onExpandBtnClick(holder, it, position)
        }
    }

    override fun getItemCount(): Int = list.size
}