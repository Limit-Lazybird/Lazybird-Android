package com.limit.lazybird.ui.ticketing

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentTicketingNoticeBinding
import com.limit.lazybird.ui.BaseFragment
import com.limit.lazybird.util.applyEscapeSequence
import com.limit.lazybird.viewmodel.TicketingViewModel
import dagger.hilt.android.AndroidEntryPoint

/************* TicketingNoticeFragment ***************
 * ??? >> 전시 상세정보 >> 예약하기 전 확인 (Fragment)
 * 예약하기 이전의 화면
 ********************************************** ***/

@AndroidEntryPoint
class TicketingNoticeFragment : BaseFragment<FragmentTicketingNoticeBinding>(FragmentTicketingNoticeBinding::inflate) {

    private val DELAY_MILLIS = 3000L // 몇 초 뒤에 다음 페이지로 넘어갈 지에 대한 값

    private val args: TicketingConfirmFragmentArgs by navArgs()
    private val viewModel: TicketingViewModel by viewModels()

    lateinit var runnable: Runnable
    lateinit var handler: Handler
    lateinit var activityResult: ActivityResultLauncher<Intent>

    lateinit var callback: OnBackPressedCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // argument 로 넘어오는 earlyBird 상세정보 ViewModel에 업데이트
        lifecycleScope.launchWhenStarted {
            viewModel.updateExhibitionInfo(args.exhibitionInfo)
        }

        viewModel.exhibitionInfo.observe(viewLifecycleOwner) { exhibitionInfo ->
            binding.ticketingNoticeContext.text =
                getString(R.string.ticketing_notice_context).applyEscapeSequence()
            moveToUrlPostDelayed(exhibitionInfo.exhibitionUrl, DELAY_MILLIS)
        }

        // 예매처로 이동 버튼 클릭
        binding.ticketingNextBtn.setOnClickListener {
            viewModel.exhibitionInfo.value?.let { exhibitionInfo ->
                cancelHandlerCallback()
                moveToUrl(exhibitionInfo.exhibitionUrl)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // activityResult init
        activityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                moveToTicketingConfirm()
            }

        // backPressed callback init
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로가기 버튼 클릭 시
                cancelHandlerCallback()
                navController.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        activityResult.unregister() // activityResult delete
        callback.remove() // backPressed callback delete
    }

    // handler Callback 취소
    private fun cancelHandlerCallback() {
        handler.removeCallbacks(runnable)
    }

    // handler Callback 등록
    private fun moveToUrlPostDelayed(url: String, millis: Long) {
        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            moveToUrl(url)
        }
        handler.postDelayed(runnable, millis)
    }

    // Url 로 웹뷰를 띄워주기
    private fun moveToUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activityResult.launch(intent)
    }

    // TicketingConfirmFragment 로 이동
    private fun moveToTicketingConfirm() {
        navController.navigate(TicketingNoticeFragmentDirections.actionTicketingNoticeFragmentToTicketingConfirmFragment(viewModel.exhibitionInfo.value!!))
    }

}