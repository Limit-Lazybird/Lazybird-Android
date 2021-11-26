package com.xemic.lazybird.ui.mybird

import android.text.Html
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.ItemExhibitionBinding
import com.xemic.lazybird.databinding.ItemReservationBinding
import com.xemic.lazybird.models.ExhibitionInfoShort
import com.xemic.lazybird.util.calculateDateDiff
import com.xemic.lazybird.util.thousandUnitFormatted
import com.xemic.lazybird.util.toDate
import com.xemic.lazybird.util.toDate2
import java.util.*

class ReservationDetailAdapter(
    val list: List<ExhibitionInfoShort>
) : RecyclerView.Adapter<ReservationDetailAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(holder: ViewHolder, view: View, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    private lateinit var binding: ItemReservationBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exhibitBinding = binding
        val exhibitionThumbnail = binding.itemReservationThumbnail
        val exhibitionTitle = binding.itemReservationTitle
        val exhibitionPlace = binding.itemReservationPlace
        val exhibitionDate = binding.itemReservationDate
        val exhibitionDiscount = binding.itemReservationDiscount
        val exhibitionPriceDc = binding.itemReservationPriceDc
        val exhibitionPrice = binding.itemReservationPrice
        val exhibitionDday = binding.itemReservationDday
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_reservation, parent, false)
        binding = ItemReservationBinding.bind(view)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.exhibitionTitle.text = list[position].title
        holder.exhibitionPlace.text = list[position].place
        holder.exhibitionDate.text = "${list[position].startDate} ~ ${list[position].endDate}"
        holder.exhibitBinding.discount = list[position].discount
        holder.exhibitionPriceDc.text = "${list[position].discountedPrice.thousandUnitFormatted()}"
        holder.exhibitionPrice.text = Html.fromHtml(
            "<strike>${list[position].price.thousandUnitFormatted()}</strike>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        holder.exhibitionDday.text = "D - ${calculateDateDiff(list[position].startDate.toDate2(), Calendar.getInstance().time)}"

        Glide.with(holder.itemView)
            .load(list[position].thumbnailImageUrl)
            .apply(
                RequestOptions().transform(
                    CenterCrop(),
                    RoundedCorners(
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            10f,
                            holder.itemView.resources.displayMetrics
                        ).toInt()
                    )
                )
            )
            .into(holder.exhibitionThumbnail)

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(holder, it, position)
        }
    }

    override fun getItemCount(): Int = list.size
}