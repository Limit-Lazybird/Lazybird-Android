package com.limit.lazybird.ui.mybird

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentReservationDetailBinding
import com.limit.lazybird.models.retrofit.Exhbt
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.ui.earilybirdDetail.EarlyBirdDetailViewModel
import com.limit.lazybird.ui.earlybirdDetail.EarlyBirdDetailFragment
import com.limit.lazybird.ui.exhibitionDetail.ExhibitionDetailFragment
import com.limit.lazybird.ui.exhibitionDetail.ExhibitionDetailViewModel
import com.limit.lazybird.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint

/************* ReservationDetailFragment ***************
 * 메인화면(마이버드 탭) >> 내가 예약한 전시리스트 보기 (Fragment)
 * 내가 예약한 전시리스트 보기
 ********************************************** ***/
@AndroidEntryPoint
class ReservationDetailFragment: Fragment(R.layout.fragment_reservation_detail) {

    companion object {
        const val TAG = "ReservationDetailFragment"
    }

    lateinit var binding: FragmentReservationDetailBinding
    private val viewModel: MyBirdViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentReservationDetailBinding.bind(view)

        viewModel.reservationExhibitionShortList.observe(viewLifecycleOwner) { exhibitList ->
            if(exhibitList.isEmpty()){
                binding.reservationDetailNoItem.visibility = View.VISIBLE
            } else {
                binding.reservationDetailNoItem.visibility = View.INVISIBLE
            }
            binding.reservationDetailRecyclerView.adapter = ReservationDetailAdapter(exhibitList).apply {
                itemClickListener = object : ReservationDetailAdapter.OnItemClickListener {
                    override fun onItemClick(
                        holder: ReservationDetailAdapter.ViewHolder,
                        view: View,
                        position: Int
                    ) {
                        // 아이템 클릭 시 (상세페이지 이동)
                        val exhibitionInfo = viewModel.getReservationExhibitionInfo(position)
                        moveToExhibitionDetail(exhibitionInfo)
                    }

                    override fun onItemLongClick(
                        holder: ReservationDetailAdapter.ViewHolder,
                        view: View,
                        position: Int
                    ) {
                        // 아이템 길게 클릭 시 (삭제 할 지)
                        // Todo : 추가하기 (CustomDialogFragment 추가하면 될 듯)
                    }
                }
            }
        }

        binding.reservationDetailBackBtn.setOnClickListener {
            // 뒤로가기 버튼 클릭
            parentActivity.supportFragmentManager.popBackStack()
        }
    }

    private fun moveToExhibitionDetail(exhibitionInfo: Exhbt) {
        // ExhibitionDetail 화면으로 이동
        when (exhibitionInfo.eb_yn) {
            "Y" -> {
                val bundle = Bundle().apply {
                    putParcelable(EarlyBirdDetailViewModel.EARLYBIRD_INFO, exhibitionInfo)
                }
                parentActivity.supportFragmentManager.replaceFragment(
                    EarlyBirdDetailFragment().apply {
                        arguments = bundle
                    },
                    true
                )
            }

            "N" -> {
                val bundle = Bundle().apply {
                    putParcelable(ExhibitionDetailViewModel.EXHIBITION_INFO, exhibitionInfo)
                }
                parentActivity.supportFragmentManager.replaceFragment(
                    ExhibitionDetailFragment().apply {
                        arguments = bundle
                    },
                    true
                )
            }
        }
    }
}