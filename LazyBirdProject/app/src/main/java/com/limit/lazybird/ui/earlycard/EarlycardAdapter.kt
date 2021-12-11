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

/************* EarlycardAdapter ***************
 * 메인화면(?? 탭) >> 얼리카드 화면 (Recycler Adapter)
 * 얼라키드 정보 리스트로 보기
 ********************************************** ***/
class EarlycardAdapter(
    val list: List<EarlycardInfo>
) : RecyclerView.Adapter<EarlycardAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(holder: EarlycardAdapter.ViewHolder, view: View, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    private lateinit var binding: ItemEarlycardBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val earlyCardTitle = binding.itemEarlycardTitle
        val earlyCardNumber = binding.itemEarlycardNumber
        val earlyCardImage = binding.itemEarlycardImg
        val earlyCardVisitedDate = binding.itemEarlycardVisitedDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_earlycard, parent, false)
        binding = ItemEarlycardBinding.bind(view)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.earlyCardTitle.text = list[position].title
        holder.earlyCardNumber.text = "No. ${list[position].no}"
        holder.earlyCardVisitedDate.text = "관람  ${if(list[position].visitDate!="N") list[position].visitDate else "-"}"
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