package com.limit.lazybird.ui.ticketing

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.limit.lazybird.R
import com.limit.lazybird.ui.custom.CustomSnackBar
import com.limit.lazybird.databinding.FragmentGetEarlycardBinding
import com.limit.lazybird.ui.BaseFragment
import com.limit.lazybird.viewmodel.TicketingViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GetEarlyCardFragment :
    BaseFragment<FragmentGetEarlycardBinding>(FragmentGetEarlycardBinding::inflate) {

    private val args: GetEarlyCardFragmentArgs by navArgs()
    private val viewModel: TicketingViewModel by viewModels()

    private lateinit var snackBar: CustomSnackBar
    private lateinit var mDetector: GestureDetectorCompat
    private var isSwipe = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this

        // argument 로 넘어오는 earlyBird 상세정보 ViewModel에 업데이트
        lifecycleScope.launchWhenStarted {
            viewModel.updateExhibitionInfo(args.exhibitionInfo)
        }

        initDetector(view)
        initSnackbar()

        // main earlycard, sub earlycard 모두 초기화하기
        viewModel.exhibitionInfo.observe(viewLifecycleOwner) { exhibitionInfo ->
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

        // No 값 가져오기
        viewModel.earlycardList.observe(viewLifecycleOwner) { earlycardList ->
            var currentNumber = 0
            earlycardList.forEach {
                currentNumber = maxOf(currentNumber, it.no)
            }
            binding.getEarlycardMainNumber.text = "NO. $currentNumber"
            binding.getEarlycardSubNumber.text = "NO. $currentNumber"
        }
    }

    // 뒤로가기 버튼 클릭 시
    fun clickBackBtn() {
        navController.popBackStack()
    }

    private fun initSnackbar() {
        snackBar = CustomSnackBar.make(requireView(), "얼리카드가 추가되었어요", "보러가기").apply {
            clickListener = object : CustomSnackBar.OnClickListener {
                override fun onClick(view: View) {
                    moveToMain()
                    dismiss()
                }
            }
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

    // SnackBar 보여주기
    private fun showSnackBar() {
        snackBar.show()
    }

    // Main 화면으로 이동
    private fun moveToMain() {
        repeat(2) {
            navController.popBackStack()
        }
    }

    override fun onDetach() {
        super.onDetach()
        snackBar.dismiss()
    }
}