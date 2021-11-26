package com.xemic.lazybird.ui.earlybird

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.ItemEarlybirdBinding
import com.xemic.lazybird.models.EarlyBirdInfo
import com.xemic.lazybird.ui.onboarding.OnBoardingAdapter

class EarlyBirdAdapter(
    val list: List<EarlyBirdInfo>
) : RecyclerView.Adapter<EarlyBirdAdapter.PageViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(holder: EarlyBirdAdapter.PageViewHolder, view: View, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    private lateinit var binding: ItemEarlybirdBinding

    inner class PageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pageTitle = binding.earlybirdPageTitle
        val pageDiscount = binding.earlybirdPageDiscount
        val pageImageView = binding.earlybirdPageImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_earlybird, parent, false)
        binding = ItemEarlybirdBinding.bind(view)
        return PageViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.pageTitle.text = list[position].title
        holder.pageDiscount.text = "${list[position].discount}%"
        Glide.with(holder.itemView)
            .load(list[position].imageUrl)
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(30)))
            .into(holder.pageImageView)
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(holder, it, position)
        }
    }

    override fun getItemCount(): Int = list.size


}