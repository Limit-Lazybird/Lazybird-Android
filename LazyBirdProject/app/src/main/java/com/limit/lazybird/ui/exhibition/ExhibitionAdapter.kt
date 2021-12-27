package com.limit.lazybird.ui.exhibition

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.limit.lazybird.R
import com.limit.lazybird.databinding.ItemExhibitionBinding
import com.limit.lazybird.models.ExhibitionInfoShort
import com.limit.lazybird.util.thousandUnitFormatted
import com.limit.lazybird.util.toDp

/************* ExhibitionAdapter ***************
 * 메인화면(전시 탭) (Recycler Adpater)
 * 전시 정보 전체 보기
 ********************************************** ***/
class ExhibitionAdapter(
    val list: List<ExhibitionInfoShort>
) : RecyclerView.Adapter<ExhibitionAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(holder: ViewHolder, view: View, position: Int)
        fun onLikeBtnClick(holder: ViewHolder, view: View, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    private lateinit var binding: ItemExhibitionBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exhibitionBinding = binding
        val exhibitionThumbnail = binding.itemExhibitionThumbnail // 전시회 정보 최상단 thumbnail 이미지
        val exhibitionLikeBtn = binding.itemExhibitionLikeBtn // 좋아요 버튼
        val exhibitionTitle = binding.itemExhibitionTitle // 전시 제목
        val exhibitionPlace = binding.itemExhibitionPlace // 전시 장소
        val exhibitionDate = binding.itemExhibitionDate // 전시 기간
        val exhibitionPriceDc = binding.itemExhibitionPriceDc // 할인된 가격 (할인율이 0%일 경우, itemExhibitionPrice 와 같은 값)
        val exhibitionPrice = binding.itemExhibitionPrice // 가격
        var isLike = false // 좋아요 상태
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_exhibition, parent, false)
        binding = ItemExhibitionBinding.bind(view)
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

        holder.isLike = list[position].isLiked

        Glide.with(holder.itemView)
            .load(list[position].thumbnailImageUrl)
            .apply(
                RequestOptions().transform(
                    CenterCrop(),
                    RoundedCorners(
                        20f.toDp(holder.itemView)
                    )
                )
            )
            .into(holder.exhibitionThumbnail)

        if(holder.isLike){
            holder.exhibitionLikeBtn.setImageResource(R.drawable.ic_fav_sm_on)
        } else {
            holder.exhibitionLikeBtn.setImageResource(R.drawable.ic_fav_sm_off)
        }

        holder.exhibitionLikeBtn.setOnClickListener {
            itemClickListener?.onLikeBtnClick(holder, it, position)
        }

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(holder, it, position)
        }

    }

    override fun getItemCount(): Int = list.size
}