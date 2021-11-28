package com.xemic.lazybird.ui.exhibitionDetail

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.xemic.lazybird.R
import com.xemic.lazybird.custom.PositionedCropTransformation
import com.xemic.lazybird.databinding.FragmentExhibitionDetailBinding
import com.xemic.lazybird.models.retrofit.Exhbt
import com.xemic.lazybird.ui.MainActivity
import com.xemic.lazybird.ui.ticketing.TicketingNoticeFragment
import com.xemic.lazybird.ui.ticketing.TicketingViewModel
import com.xemic.lazybird.util.applyEscapeSequence
import com.xemic.lazybird.util.replaceFragment
import com.xemic.lazybird.util.thousandUnitFormatted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/************* ExhibitionDetailFragment ***************
 * 메인화면(전시 탭) >> 전시 상세 전시 (Dialog)
 * 전시 정보 자세히 보기
 * Todo : 코드단에서 수동으로 안하고 ConstraintLayout 을 통해 비율 맞추도록 수정
 ********************************************** ***/
@AndroidEntryPoint
class ExhibitionDetailFragment: Fragment(R.layout.fragment_exhibition_detail) {

    companion object {
        const val TAG = "ExhibitionDetailFragment"
    }

    private val THUMBNAIL_IMAGE_RATIO = 4 / 3f  // Thumbnail 이미지의 세로 크기
    private val DETAIL_IMAGE_LIMIT_HIGH = 500f // detail Image의 최대 Hegiht 값

    private lateinit var binding: FragmentExhibitionDetailBinding
    private val viewModel: ExhibitionDetailViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    lateinit var callback: OnBackPressedCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentExhibitionDetailBinding.bind(view)

        lifecycleScope.launchWhenStarted {
            // argument 로 넘어오는 earlyBird 상세정보 ViewModel에 업데이트
            val exhbt = requireArguments().getParcelable<Exhbt>(ExhibitionDetailViewModel.EXHIBITION_INFO)
            if(exhbt!=null)
                viewModel.updateExhibitionInfo(exhbt)
        }

        viewModel.exhibitionInfo.observe(viewLifecycleOwner) { exhibitionInfo ->
            // exhibitionInfo 정보 업데이트 완료
            binding.exhibitionDetailDDay.text = "D - ${exhibitionInfo.dDay}" // 전시 시작까지 남은 기간
            binding.exhibitionDetailTitle.text = exhibitionInfo.title // 전시 제목
            binding.exhibitionDetailPlace.text = exhibitionInfo.place // 전시 장소
            binding.exhibitionDetailDate.text =
                "${exhibitionInfo.startDate}~${exhibitionInfo.endDate}" // 전시 기간
            binding.discount = exhibitionInfo.discount // 할인율
            binding.exhibitionDetailPriceDc.text = exhibitionInfo.discountedPrice.thousandUnitFormatted() // 할인 가격
            binding.exhibitionDetailPrice.text = exhibitionInfo.price.thousandUnitFormatted() // 가격
            binding.notice = exhibitionInfo.notice.applyEscapeSequence() // 전시 공지사항

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

            binding.exhibitionDetailMoreBtn.setOnClickListener {
                // 상세정보 더보기 버튼 클릭 시
                binding.exhibitionDetailMoreBtn.visibility = View.INVISIBLE // 상세보기 버튼 안보이게 처리

                // 상세 이미지 높이값 WRAP_CONTENT 로 바꾸고, 다시 Image 가져오기
                binding.exhibitionDetailImg.layoutParams.height =
                    ViewGroup.LayoutParams.WRAP_CONTENT
                Glide.with(this)
                    .load(exhibitionInfo.exhibitionDetailImgUrl)
                    .override(Target.SIZE_ORIGINAL) // 이미지 깨짐 방지
                    .into(binding.exhibitionDetailImg)
            }
            binding.exhibitionDetailLikeBtn.setOnClickListener {
                // 좋아요 버튼 클릭
                viewLifecycleOwner.lifecycle.coroutineScope.launch {
                    viewModel.clickLike()
                }
            }
        }

        viewModel.exhibitionLike.observe(viewLifecycleOwner) { isLike ->
            // 좋아요 상태 변경
            if(isLike) {
                binding.exhibitionDetailLikeBtn.setImageResource(R.drawable.ic_fav_lg_on)
            } else {
                binding.exhibitionDetailLikeBtn.setImageResource(R.drawable.ic_fav_lg_off)
            }
        }

        binding.exhibitionDetailTicketingBtn.setOnClickListener {
            // TicketingNoticeFragment 화면 이동
            val bundle = Bundle().apply {
                putParcelable(TicketingViewModel.EXHIBITION_INFO, viewModel.exhibitionInfo.value!!)
            }
            parentActivity.supportFragmentManager.replaceFragment(TicketingNoticeFragment().apply {
                arguments = bundle
            })
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로가기 버튼 클릭 시
                parentActivity.supportFragmentManager.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}