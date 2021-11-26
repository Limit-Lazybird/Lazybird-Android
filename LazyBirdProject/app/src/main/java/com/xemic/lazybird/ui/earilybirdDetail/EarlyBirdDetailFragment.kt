package com.xemic.lazybird.ui.earlybirdDetail

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.text.HtmlCompat
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
import com.xemic.lazybird.databinding.FragmentEarlybirdDetailBinding
import com.xemic.lazybird.models.retrofit.Exhbt
import com.xemic.lazybird.ui.MainActivity
import com.xemic.lazybird.ui.earilybirdDetail.EarlyBirdDetailViewModel
import com.xemic.lazybird.ui.ticketing.TicketingNoticeFragment
import com.xemic.lazybird.ui.ticketing.TicketingViewModel
import com.xemic.lazybird.util.applyEscapeSequence
import com.xemic.lazybird.util.replaceFragment
import com.xemic.lazybird.util.thousandUnitFormatted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EarlyBirdDetailFragment : Fragment(R.layout.fragment_earlybird_detail) {

    companion object {
        const val TAG = "EarlyBirdDetailFragment"
    }

    private val THUMBNAIL_IMAGE_RATIO = 4 / 3f
    private val DETAIL_IMAGE_LIMIT_HIGH = 500f

    private lateinit var binding: FragmentEarlybirdDetailBinding
    private val viewModel: EarlyBirdDetailViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    lateinit var callback: OnBackPressedCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEarlybirdDetailBinding.bind(view)

        lifecycleScope.launchWhenStarted {
            val exhbt = requireArguments().getParcelable<Exhbt>(EarlyBirdDetailViewModel.EARLYBIRD_INFO)
            if(exhbt!=null)
                viewModel.updateExhibitionInfo(exhbt)
        }

        viewModel.exhibitionInfo.observe(viewLifecycleOwner) { exhibitionInfo ->
            binding.earlybirdDetailDDay.text = "D - ${exhibitionInfo.dDay}"
            binding.earlybirdDetailTitle.text = exhibitionInfo.title
            binding.earlybirdDetailPlace.text = exhibitionInfo.place
            binding.earlybirdDetailDate.text =
                "${exhibitionInfo.startDate}~${exhibitionInfo.endDate}"
            binding.earlybirdDetailDiscount.text = "${exhibitionInfo.discount}%"
            binding.earlybirdDetailPriceDc.text =
                "${exhibitionInfo.discountedPrice.thousandUnitFormatted()}"
            binding.earlybirdDetailPrice.text = Html.fromHtml(
                "<strike>${exhibitionInfo.price.thousandUnitFormatted()}</strike>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            binding.notice = exhibitionInfo.notice.applyEscapeSequence()

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
                        binding.earlybirdDetailThumbnail.layoutParams.height =
                            (binding.earlybirdDetailThumbnail.layoutParams.width * THUMBNAIL_IMAGE_RATIO).toInt()
                        return false
                    }

                })
                .into(binding.earlybirdDetailThumbnail)

            // detail Image
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
                            binding.earlybirdDetailImg.layoutParams.height = limitHeight.toInt()
                            binding.earlybirdDetailMoreBtn.visibility = View.VISIBLE
                        }
                        return false
                    }
                })
                .into(binding.earlybirdDetailImg)

            binding.earlybirdDetailMoreBtn.setOnClickListener {
                // 상세정보 더보기 버튼 클릭 시
                binding.earlybirdDetailMoreBtn.visibility = View.INVISIBLE // 상세보기 버튼 안보이게 처리

                // 상세 이미지 높이값 WRAP_CONTENT 로 바꾸고, 다시 Image 가져오기
                binding.earlybirdDetailImg.layoutParams.height =
                    ViewGroup.LayoutParams.WRAP_CONTENT
                Glide.with(this)
                    .load(exhibitionInfo.exhibitionDetailImgUrl)
                    .override(Target.SIZE_ORIGINAL) // 이미지 깨짐 방지
                    .into(binding.earlybirdDetailImg)
            }
            binding.earlybirdDetailLikeBtn.setOnClickListener {
                // 좋아요 버튼 클릭
                viewLifecycleOwner.lifecycle.coroutineScope.launch {
                    viewModel.clickLike()
                }
            }
        }

        viewModel.exhibitionLike.observe(viewLifecycleOwner) { isLike ->
            if (isLike) {
                binding.earlybirdDetailLikeBtn.setImageResource(R.drawable.ic_fav_lg_on)
            } else {
                binding.earlybirdDetailLikeBtn.setImageResource(R.drawable.ic_fav_lg_off)
            }
        }

        binding.earlybirdDetailTicketingBtn.setOnClickListener {
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