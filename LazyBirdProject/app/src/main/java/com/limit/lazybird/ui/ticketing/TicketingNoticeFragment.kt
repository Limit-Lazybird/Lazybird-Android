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
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentTicketingNoticeBinding
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.util.applyEscapeSequence
import com.limit.lazybird.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint

/************* TicketingNoticeFragment ***************
 * ??? >> 전시 상세정보 >> 예약하기 전 확인 (Fragment)
 * 예약하기 이전의 화면
 ********************************************** ***/

@AndroidEntryPoint
class TicketingNoticeFragment : Fragment(R.layout.fragment_ticketing_notice) {

    companion object {
        const val TAG = "TicketingNoticeFragment"
    }

    private val DELAY_MILLIS = 3000L // 몇 초 뒤에 다음 페이지로 넘어갈 지에 대한 값

    lateinit var binding: FragmentTicketingNoticeBinding
    private val viewModel: TicketingViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    lateinit var runnable: Runnable
    lateinit var handler: Handler
    lateinit var activityResult: ActivityResultLauncher<Intent>

    lateinit var callback: OnBackPressedCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTicketingNoticeBinding.bind(view)

        viewModel.updateExhibitionInfo(
            requireArguments().getParcelable(
                TicketingViewModel.EXHIBITION_INFO
            )!!
        )

        viewModel.exhibitionInfo.observe(viewLifecycleOwner) { exhibitionInfo ->
            binding.ticketingNoticeContext.text =
                getString(R.string.ticketing_notice_context).applyEscapeSequence()
            moveToUrlPostDelayed(exhibitionInfo.exhibitionUrl, DELAY_MILLIS)
        }

        binding.ticketingNextBtn.setOnClickListener {
            // 예매처로 이동 버튼 클릭
            viewModel.exhibitionInfo.value?.let { exhibitionInfo ->
                cancelHandlerCallback()
                moveToUrl(exhibitionInfo.exhibitionUrl)
            }
        }
    }

    private fun cancelHandlerCallback() {
        // handler Callback 취소
        handler.removeCallbacks(runnable)
    }

    private fun moveToUrlPostDelayed(url: String, millis: Long) {
        // handler Callback 등록
        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            moveToUrl(url)
        }
        handler.postDelayed(runnable, millis)
    }

    private fun moveToUrl(url: String) {
        // Url 로 웹뷰를 띄워주기
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activityResult.launch(intent)
    }

    private fun moveToTicketingConfirm() {
        // TicketingConfirmFragment 로 이동
        val bundle = Bundle().apply {
            putParcelable(TicketingViewModel.EXHIBITION_INFO, viewModel.exhibitionInfo.value!!)
        }
        parentActivity.supportFragmentManager.replaceFragment(
            TicketingConfirmFragment().apply {
                arguments = bundle
            },
            false
        )
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
                parentActivity.supportFragmentManager.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        activityResult.unregister() // activityResult delete
        callback.remove() // backPressed callback delete
    }
}