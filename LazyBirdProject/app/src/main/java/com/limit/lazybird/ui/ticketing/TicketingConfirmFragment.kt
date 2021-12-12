package com.limit.lazybird.ui.ticketing

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.bumptech.glide.Glide
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentTicketingConfirmBinding
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.util.thousandUnitFormatted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/************* ExhibitionFragment ***************
 * ??? >> 전시 상세정보 >> 예약하기 전 확인 >> 예약 완료화면 (Fragment)
 * 예약 완료 후 확인하는 화면
 ********************************************** ***/
@AndroidEntryPoint
class TicketingConfirmFragment : Fragment(R.layout.fragment_ticketing_confirm) {

    companion object {
        const val TAG = "TicketingConfirmFragment"
    }

    lateinit var binding: FragmentTicketingConfirmBinding
    private val viewModel: TicketingViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTicketingConfirmBinding.bind(view)

        viewModel.updateExhibitionInfo(
            requireArguments().getParcelable(
                TicketingViewModel.EXHIBITION_INFO
            )!!
        )

        viewModel.exhibitionInfo.observe(viewLifecycleOwner) { exhibitionInfo ->
            binding.ticketingConfirmExhbtTitle.text = exhibitionInfo.title
            binding.ticketingConfirmExhbtDate.text =
                "${exhibitionInfo.startDate}~${exhibitionInfo.endDate}"
            binding.ticketingConfirmExhbtPlace.text = exhibitionInfo.place
            binding.ticketingConfirmExhbtPrice.text = Html.fromHtml(
                "<strike>${exhibitionInfo.price.thousandUnitFormatted()}</strike>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            binding.discount = exhibitionInfo.discount
            binding.ticketingConfirmExhbtPriceDc.text = exhibitionInfo.discountedPrice.thousandUnitFormatted()
            Glide.with(this)
                .load(exhibitionInfo.thumbnailImageUrl)
                .centerCrop()
                .into(binding.ticketingConfirmExhbtImg)
        }

        binding.ticketingConfirmOkBtn.setOnClickListener { 
            // 예매 완료 버튼 클릭 시
            binding.ticketingConfirmOkBtn.isClickable = false // 중복클릭 차단
            viewLifecycleOwner.lifecycle.coroutineScope.launch {
                viewModel.updateExhibitionReservation()
                moveToBack() // Todo : 얼리카드 발급 화면 이동
            }
        }

        binding.ticketingConfirmCancelBtn.setOnClickListener {
            // 돌아가기 버튼 클릭 시
            moveToBack()
        }

        binding.ticketingConfirmBackBtn.setOnClickListener {
            // 뒤로가기 버튼 클릭 시
            moveToBack()
        }
    }

    private fun moveToBack() {
        // 메인화면으로 되돌아가기
        repeat(1){
            parentActivity.supportFragmentManager.popBackStack()
        }
    }
}