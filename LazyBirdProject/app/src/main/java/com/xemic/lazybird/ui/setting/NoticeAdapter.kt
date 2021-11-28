package com.xemic.lazybird.ui.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.ItemNoticeBinding
import com.xemic.lazybird.models.NoticeInfo
import com.xemic.lazybird.util.applyEscapeSequence

/***************** NoticeAdapter *******************
 * 메인화면(마이버드 탭) >> 옵션 >> 공지사항 (Recycler Adpater)
 * 공지사항 전체 보기
 ********************************************** ***/
class NoticeAdapter(
    val list: List<NoticeInfo>
): RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onExpandBtnClick(holder: NoticeAdapter.ViewHolder, view: View, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    private lateinit var binding: ItemNoticeBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val noticeDate = binding.itemNoticeDate // 공지 날짜
        val noticeTitle = binding.itemNoticeTitle // 공지 제목
        val noticeContext = binding.itemNoticeContext // 공지 내용
        val noticeExpandBtn = binding.itemNoticeExpandBtn // 확장 버튼
        val isExpanded = MutableLiveData(false) // 확장 상태
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