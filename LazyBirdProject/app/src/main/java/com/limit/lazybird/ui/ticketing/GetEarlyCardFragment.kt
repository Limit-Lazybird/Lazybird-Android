package com.limit.lazybird.ui.ticketing

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentGetEarlycardBinding
import com.limit.lazybird.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GetEarlyCardFragment:Fragment(R.layout.fragment_get_earlycard) {

    companion object {
        const val TAG = "TicketingConfirmFragment"
    }

    lateinit var binding: FragmentGetEarlycardBinding
    private val viewModel: TicketingViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }
    private lateinit var mDetector: GestureDetectorCompat
    private var isSwipe = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGetEarlycardBinding.bind(view)
        viewModel.updateExhibitionInfo(
            requireArguments().getParcelable(
                TicketingViewModel.EXHIBITION_INFO
            )!!
        )
        viewModel.exhibitionInfo.observe(viewLifecycleOwner) { exhibitionInfo ->
            binding.getEarlycardMainTitle.text = exhibitionInfo.title
            binding.getEarlycardSubTitle.text = exhibitionInfo.title
            binding.getEarlycardMainNumber.text = "NO. X"
            binding.getEarlycardSubNumber.text = "NO. X"
            Glide.with(this)
                .load(exhibitionInfo.thumbnailImageUrl)
                .centerCrop()
                .into(binding.getEarlycardMainImg)
            Glide.with(this)
                .load(exhibitionInfo.thumbnailImageUrl)
                .centerCrop()
                .into(binding.getEarlycardSubImg)
        }
        initDetector(view)
    }


    private fun initDetector(view:View) {
        mDetector = GestureDetectorCompat(requireContext(), object : GestureDetector.OnGestureListener {
            override fun onDown(p0: MotionEvent?): Boolean {
                return true
            }

            override fun onShowPress(p0: MotionEvent?) {
            }

            override fun onSingleTapUp(p0: MotionEvent?): Boolean {
                return true
            }

            override fun onScroll(
                p0: MotionEvent?,
                p1: MotionEvent?,
                p2: Float,
                p3: Float
            ): Boolean {
                return true
            }

            override fun onLongPress(p0: MotionEvent?) {
            }

            override fun onFling(
                p0: MotionEvent?,
                p1: MotionEvent?,
                p2: Float,
                p3: Float
            ): Boolean {
                startAnimation()
                isSwipe = true
                return true
            }
        })
        view.setOnTouchListener { view, motionEvent ->
            !isSwipe && mDetector.onTouchEvent(motionEvent)
        }
    }

    private fun startAnimation() {
        binding.getEarlycardSubContainer.startAnimation(
            AnimationUtils.loadAnimation(context, R.anim.ticket_up).apply {
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(p0: Animation?) {
                    }
                    override fun onAnimationRepeat(p0: Animation?) {
                    }
                    override fun onAnimationEnd(p0: Animation?) {
                        binding.getEarlycardMainContainer.visibility = View.VISIBLE // 메인 보이기
                        binding.getEarlycardSubContainer.visibility = View.INVISIBLE // 서브 숨기기
                        binding.getEarlycardSubContainer.clearAnimation() // 서브 animation 설정 제거
                    }
                })
            }
        )
    }
}