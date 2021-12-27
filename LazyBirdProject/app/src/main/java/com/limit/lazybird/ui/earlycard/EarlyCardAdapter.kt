package com.limit.lazybird.ui.earlycard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.limit.lazybird.R
import com.limit.lazybird.databinding.ItemEarlycardBinding
import com.limit.lazybird.models.EarlycardInfo

/************* EarlyCardAdapter ***************
 * 메인화면(?? 탭) >> 얼리카드 화면 (Recycler Adapter)
 * 얼라키드 정보 리스트로 보기
 ********************************************** ***/
class EarlyCardAdapter(
    val list: List<EarlycardInfo>
) : RecyclerView.Adapter<EarlyCardAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(holder: EarlyCardAdapter.ViewHolder, view: View, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    private lateinit var binding: ItemEarlycardBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val earlyCardTitle = binding.itemEarlycardTitle // 얼리카드 제목
        val earlyCardNumber = binding.itemEarlycardNumber // 얼리카드의 번호
        val earlyCardImage = binding.itemEarlycardImg // 얼리카드 이미지
        val earlyCardVisitedDate = binding.itemEarlycardVisitedDate // 얼리카드 방문 날짜 (없으면 "-" 으로 표시)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_earlycard, parent, false)
        binding = ItemEarlycardBinding.bind(view)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.earlyCardTitle.text = list[position].title
        holder.earlyCardNumber.text = "NO. ${list[position].no}"
        holder.earlyCardVisitedDate.text = list[position].visitDate
        Glide.with(holder.itemView)
            .load(list[position].imgUrl)
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(20)))
            .into(holder.earlyCardImage)
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(holder, it, position)
        }
    }

    override fun getItemCount(): Int = list.size


}