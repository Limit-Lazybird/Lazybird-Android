package com.limit.lazybird.ui.ticketing

import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.limit.lazybird.R
import com.limit.lazybird.custom.CustomSnackBar
import com.limit.lazybird.databinding.FragmentGetEarlycardBinding
import com.limit.lazybird.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_bs_date_select.*


@AndroidEntryPoint
class GetEarlyCardFragment : Fragment(R.layout.fragment_get_earlycard) {

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

        initDetector(view)

        viewModel.updateExhibitionInfo(
            requireArguments().getParcelable(
                TicketingViewModel.EXHIBITION_INFO
            )!!
        )

        viewModel.exhibitionInfo.observe(viewLifecycleOwner) { exhibitionInfo ->
            // main earlycard, sub earlycard 모두 초기화하기
            binding.getEarlycardMainTitle.text = exhibitionInfo.title
            binding.getEarlycardSubTitle.text = exhibitionInfo.title
            Glide.with(this)
                .load(exhibitionInfo.thumbnailImageUrl)
                .centerCrop()
                .into(binding.getEarlycardMainImg)
            Glide.with(this)
                .load(exhibitionInfo.thumbnailImageUrl)
                .centerCrop()
                .into(binding.getEarlycardSubImg)
        }

        viewModel.earlycardList.observe(viewLifecycleOwner) { earlycardList ->
            // No 값 가져오기
            var currentNumber = 0
            earlycardList.forEach {
                currentNumber = maxOf(currentNumber, it.no)
            }
            binding.getEarlycardMainNumber.text = "NO. $currentNumber"
            binding.getEarlycardSubNumber.text = "NO. $currentNumber"
        }
        binding.getEarlycardBackBtn.setOnClickListener {
            // 뒤로가기 버튼 클릭 시
            parentActivity.supportFragmentManager.popBackStack()
        }
    }


    private fun initDetector(view: View) {
        mDetector =
            GestureDetectorCompat(requireContext(), object : GestureDetector.OnGestureListener {
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
                        showSnackBar()
                    }
                })
            }
        )
    }

    private fun showSnackBar() {
        CustomSnackBar.make(requireView(), "얼리카드가 추가되었어요", "보러가기").apply {
            clickListener = object : CustomSnackBar.OnClickListener {
                override fun onClick(view: View) {
                    moveToMain()
                    dismiss()
                }
            }
        }.show()
    }

    private fun moveToMain(){
        repeat(2) {
            parentActivity.supportFragmentManager.popBackStack()
        }
    }

}