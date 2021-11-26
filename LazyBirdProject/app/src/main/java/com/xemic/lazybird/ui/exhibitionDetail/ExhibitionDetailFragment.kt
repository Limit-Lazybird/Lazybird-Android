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

@AndroidEntryPoint
class ExhibitionDetailFragment: Fragment(R.layout.fragment_exhibition_detail) {

    companion object {
        const val TAG = "EarlyBirdDetailFragment"
    }

    private val THUMBNAIL_IMAGE_RATIO = 4 / 3f
    private val DETAIL_IMAGE_LIMIT_HIGH = 500f

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
            val exhbt = requireArguments().getParcelable<Exhbt>(ExhibitionDetailViewModel.EXHIBITION_INFO)
            if(exhbt!=null)
                viewModel.updateExhibitionInfo(exhbt)
        }

        viewModel.exhibitionInfo.observe(viewLifecycleOwner) { exhibitionInfo ->
            binding.exhibitionDetailDDay.text = "D - ${exhibitionInfo.dDay}"
            binding.exhibitionDetailTitle.text = exhibitionInfo.title
            binding.exhibitionDetailPlace.text = exhibitionInfo.place
            binding.exhibitionDetailDate.text =
                "${exhibitionInfo.startDate}~${exhibitionInfo.endDate}"
            binding.exhibitionDetailPrice.text = exhibitionInfo.price.thousandUnitFormatted()
            binding.exhibitionDetailPriceDc.text = exhibitionInfo.discountedPrice.thousandUnitFormatted()
            binding.discount = exhibitionInfo.discount
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
            if(isLike) {
                binding.exhibitionDetailLikeBtn.setImageResource(R.drawable.ic_fav_lg_on)
            } else {
                binding.exhibitionDetailLikeBtn.setImageResource(R.drawable.ic_fav_lg_off)
            }
        }

        binding.exhibitionDetailTicketingBtn.setOnClickListener {
            // 티켓 예매 화면으로 넘어가기
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