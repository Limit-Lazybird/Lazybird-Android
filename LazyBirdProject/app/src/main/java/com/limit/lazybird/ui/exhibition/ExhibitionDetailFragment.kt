package com.limit.lazybird.ui.exhibition

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.limit.lazybird.R
import com.limit.lazybird.ui.custom.PositionedCropTransformation
import com.limit.lazybird.databinding.FragmentExhibitionDetailBinding
import com.limit.lazybird.ui.BaseFragment
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.util.applyEscapeSequenceWithDot
import com.limit.lazybird.util.thousandUnitFormatted
import com.limit.lazybird.viewmodel.ExhibitionDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

/************* ExhibitionDetailFragment ***************
 * 메인화면(전시 탭) >> 전시 상세 전시 (Dialog)
 * 전시 정보 자세히 보기
 ********************************************** ***/
@AndroidEntryPoint
class ExhibitionDetailFragment: BaseFragment<FragmentExhibitionDetailBinding>(FragmentExhibitionDetailBinding::inflate) {

    private val THUMBNAIL_IMAGE_RATIO = 4 / 3f  // Thumbnail 이미지의 세로 크기
    private val DETAIL_IMAGE_LIMIT_HIGH = 500f // detail Image의 최대 Hegiht 값

    private val args: ExhibitionDetailFragmentArgs by navArgs()
    private val viewModel: ExhibitionDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // argument 로 넘어오는 earlyBird 상세정보 ViewModel에 업데이트
        lifecycleScope.launchWhenStarted {
            viewModel.updateExhibitionInfo(args.earlyBirdInfo)
        }

        // exhibitionInfo 정보 업데이트 완료
        viewModel.exhibitionInfo.observe(viewLifecycleOwner) { exhibitionInfo ->
            binding.exhibitionDetailDDay.text = "D - ${exhibitionInfo.dDay}" // 전시 시작까지 남은 기간
            binding.exhibitionDetailTitle.text = exhibitionInfo.title // 전시 제목
            binding.exhibitionDetailPlace.text = exhibitionInfo.place // 전시 장소
            binding.exhibitionDetailDate.text =
                "${exhibitionInfo.startDate}~${exhibitionInfo.endDate}" // 전시 기간
            binding.discount = exhibitionInfo.discount // 할인율
            binding.exhibitionDetailPriceDc.text = exhibitionInfo.discountedPrice.thousandUnitFormatted() // 할인 가격
            binding.exhibitionDetailPrice.text = exhibitionInfo.price.thousandUnitFormatted() // 가격
            binding.notice = exhibitionInfo.notice.applyEscapeSequenceWithDot() // 전시 공지사항

            // thumbnail Image
            Glide.with(this)
                .load(exhibitionInfo.thumbnailImageUrl)
                .transform(PositionedCropTransformation(context, 0.5f, 0f))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.exhibitionDetailThumbnail.layoutParams.height =
                            (binding.exhibitionDetailThumbnail.layoutParams.width * THUMBNAIL_IMAGE_RATIO).toInt()
                        return false
                    }

                })
                .into(binding.exhibitionDetailThumbnail)

            // detail image
            Glide.with(this)
                .load(exhibitionInfo.exhibitionDetailImgUrl)
                .transform(PositionedCropTransformation(context, 0.5f, 0f))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        val limitHeight = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            DETAIL_IMAGE_LIMIT_HIGH,
                            resources.displayMetrics
                        )
                        if (resource != null && limitHeight < resource.intrinsicHeight) {
                            binding.exhibitionDetailImg.layoutParams.height = limitHeight.toInt()
                            binding.exhibitionDetailMoreBtn.visibility = View.VISIBLE
                        }
                        return false
                    }
                })
                .into(binding.exhibitionDetailImg)

            // 상세정보 더보기 버튼 클릭 시
            binding.exhibitionDetailMoreBtn.setOnClickListener {
                binding.exhibitionDetailMoreBtn.visibility = View.INVISIBLE // 상세보기 버튼 안보이게 처리

                // 상세 이미지 높이값 WRAP_CONTENT 로 바꾸고, 다시 Image 가져오기
                binding.exhibitionDetailImg.layoutParams.height =
                    ViewGroup.LayoutParams.WRAP_CONTENT

                Glide.with(this)
                    .load(exhibitionInfo.exhibitionDetailImgUrl)
                    .override(Target.SIZE_ORIGINAL) // 이미지 깨짐 방지
                    .into(binding.exhibitionDetailImg)
            }

            // 좋아요 버튼 클릭
            binding.exhibitionDetailLikeBtn.setOnClickListener {
                viewModel.clickLike()
            }
        }

        // 좋아요 상태 변경
        viewModel.exhibitionLike.observe(viewLifecycleOwner) { isLike ->
            if(isLike) {
                binding.exhibitionDetailLikeBtn.setImageResource(R.drawable.ic_fav_lg_on)
            } else {
                binding.exhibitionDetailLikeBtn.setImageResource(R.drawable.ic_fav_lg_off)
            }
        }

        // TicketingNoticeFragment 화면 이동
        binding.exhibitionDetailTicketingBtn.setOnClickListener {
            moveToTicketingNoticeFragment()
        }
    }

    // TicketingNoticeFragment 로 이동
    private fun moveToTicketingNoticeFragment() {
        navController.navigate(ExhibitionDetailFragmentDirections.actionExhibitionDetailFragmentToTicketingNoticeFragment(viewModel.exhibitionInfo.value!!))
    }
}