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
import com.xemic.lazybird.databinding.ItemReservationBinding
import com.xemic.lazybird.models.ExhibitionInfoShort
import com.xemic.lazybird.util.calculateDateDiff
import com.xemic.lazybird.util.thousandUnitFormatted
import com.xemic.lazybird.util.toDate2
import java.util.*

/************* ReservationDetailAdapter ***************
 * 메인화면(마이버드 탭) >> 내가 예약한 전시리스트 보기 (Recycler Adapter)
 * 내가 예약한 전시리스트 보기
 ********************************************** ***/
class ReservationDetailAdapter(
    val list: List<ExhibitionInfoShort>
) : RecyclerView.Adapter<ReservationDetailAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(holder: ViewHolder, view: View, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    private lateinit var binding: ItemReservationBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exhibitionBinding = binding
        val exhibitionThumbnail = binding.itemReservationThumbnail // 전시회 정보 최상단 thumbnail 이미지
        val exhibitionTitle = binding.itemReservationTitle // 전시 제목
        val exhibitionPlace = binding.itemReservationPlace // 전시 장소
        val exhibitionDate = binding.itemReservationDate // 전시 기간
        val exhibitionPriceDc = binding.itemReservationPriceDc // 할인된 가격 (할인율이 0%일 경우, itemReservationPrice 와 같은 값)
        val exhibitionPrice = binding.itemReservationPrice // 가격
        val exhibitionDday = binding.itemReservationDday // 전시 시작까지 남은 기간
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
        holder.exhibitionBinding.discount = list[position].discount
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