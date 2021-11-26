package com.xemic.lazybird.ui.onboarding

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.allViews
import androidx.core.view.get
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.xemic.lazybird.R
import com.xemic.lazybird.custom.OptionItemView
import com.xemic.lazybird.models.ExhibitionCategory

/*** deprecated ***/

class OnBoardingAdapter(
    private var items: List<ExhibitionCategory>,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<OnBoardingAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(holder: OnBoardingAdapter.ViewHolder, view: View, position: Int, idx: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val categoryOptionLayout: FlexboxLayout = itemView.findViewById(R.id.categoryOptionLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 카테고리 이름
        holder.categoryName.text = items[position].categoryName
        // 카테고리 내용
        for (idx in items[position].categoryList.indices) {
            val categoryOption = items[position].categoryList[idx]
            holder.categoryOptionLayout.addView(
                OptionItemView(
                    holder.itemView.context,
                    lifecycleOwner,
                    categoryOption.optionName,
                    categoryOption.isSelected
                ).apply {
                    setOnClickListener {
                        itemClickListener?.onItemClick(holder, this, position, idx)
                    }
                }
            )
        }
    }

    override fun getItemCount(): Int = items.size


    fun setCategoryList(exhibitionCategoryList: List<ExhibitionCategory>) {
        items = exhibitionCategoryList
        notifyDataSetChanged()
    }
}