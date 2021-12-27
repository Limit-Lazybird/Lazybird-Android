package com.limit.lazybird.ui.ticketing

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentTicketingConfirmBinding
import com.limit.lazybird.ui.BaseFragment
import com.limit.lazybird.util.thousandUnitFormatted
import com.limit.lazybird.viewmodel.TicketingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/************* ExhibitionFragment ***************
 * ??? >> 전시 상세정보 >> 예약하기 전 확인 >> 예약 완료화면 (Fragment)
 * 예약 완료 후 확인하는 화면
 ********************************************** ***/
@AndroidEntryPoint
class TicketingConfirmFragment :
    BaseFragment<FragmentTicketingConfirmBinding>(FragmentTicketingConfirmBinding::inflate) {

    private val args: GetEarlyCardFragmentArgs by navArgs()
    private val viewModel: TicketingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this

        // argument 로 넘어오는 earlyBird 상세정보 ViewModel에 업데이트
        lifecycleScope.launchWhenStarted {
            viewModel.updateExhibitionInfo(args.exhibitionInfo)
        }

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
            binding.ticketingConfirmExhbtPriceDc.text =
                exhibitionInfo.discountedPrice.thousandUnitFormatted()
            Glide.with(this)
                .load(exhibitionInfo.thumbnailImageUrl)
                .centerCrop()
                .into(binding.ticketingConfirmExhbtImg)
        }
    }

    fun clickOkBtn() {
        binding.ticketingConfirmOkBtn.isClickable = false // 중복클릭 차단
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewModel.updateExhibitionReservation()
            moveToGetEarlyCard()
        }
    }

    // GetEarlyCardFragment 로 이동
    private fun moveToGetEarlyCard() {
        navController.navigate(
            TicketingConfirmFragmentDirections.actionTicketingConfirmFragmentToGetEarlyCardFragment(
                viewModel.exhibitionInfo.value!!
            )
        )
    }

    // 뒤로가기 클릭 시
    fun moveToBack() {
        navController.popBackStack()
    }
}