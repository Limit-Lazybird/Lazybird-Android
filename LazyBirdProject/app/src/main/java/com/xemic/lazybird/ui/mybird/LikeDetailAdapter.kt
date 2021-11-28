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
import com.xemic.lazybird.models.ExhibitionInfoShort
import com.xemic.lazybird.util.thousandUnitFormatted

/************* LikeDetailAdapter ***************
 * 메인화면(마이버드 탭) >> 내가 찜한 전시리스트 보기 (Recycler Adpater)
 * 내가 찜한 전시리스트 보기
 ********************************************** ***/
class LikeDetailAdapter(
    val list: List<ExhibitionInfoShort>
) : RecyclerView.Adapter<LikeDetailAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(holder: ViewHolder, view: View, position: Int)
        fun onLikeBtnClick(holder: ViewHolder, view: View, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    private lateinit var binding: ItemExhibitionBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exhibitionThumbnail = binding.itemExhibitionThumbnail // 전시 thumbnail 이미지
        val exhibitionLikeBtn = binding.itemExhibitionLikeBtn // 좋아요 버튼
        val exhibitionTitle = binding.itemExhibitionTitle // 전시 제목
        val exhibitionPlace = binding.itemExhibitionPlace // 전시 장소
        val exhibitionDate = binding.itemExhibitionDate // 전시 기간
        var exhibitionDiscount = binding.discount // 할인율
        val exhibitionPriceDc = binding.itemExhibitionPriceDc // 할인 가격
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
        holder.exhibitionDiscount = list[position].discount
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
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            20f,
                            holder.itemView.resources.displayMetrics
                        ).toInt()
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