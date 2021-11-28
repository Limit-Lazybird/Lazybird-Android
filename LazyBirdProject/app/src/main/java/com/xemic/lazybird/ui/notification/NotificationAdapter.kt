package com.xemic.lazybird.ui.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.ItemNotificationBinding
import com.xemic.lazybird.models.retrofit.NotificationInfo
import com.xemic.lazybird.ui.mybird.LikeDetailAdapter
import com.xemic.lazybird.util.applyEscapeSequence

/***************** NotificationAdapter *******************
 * ?? >> 알림화면 (Fragment)
 * 나에게 온 알림 보기
 ********************************************** ***/
class NotificationAdapter(
    val list: List<NotificationInfo>
): RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onDeleteBtnClick(holder: NotificationAdapter.ViewHolder, view: View, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    private lateinit var binding: ItemNotificationBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val notificationTime = binding.itemNotificationTime // 공지 날짜
        val notificationType = binding.itemNotificationType // 공지 제목
        val notificationContext = binding.itemNotificationContext // 공지 내용
        val deleteBtn = binding.itemNotificationDeleteBtn // 삭제 버튼
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        binding = ItemNotificationBinding.bind(view)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.notificationTime.text = list[position].time
        holder.notificationType.text = list[position].type
        holder.notificationContext.text = list[position].context
        holder.deleteBtn.setOnClickListener {
            // 삭제버튼 클릭 시
            itemClickListener?.onDeleteBtnClick(holder, it, position)
        }
    }

    override fun getItemCount(): Int = list.size
}