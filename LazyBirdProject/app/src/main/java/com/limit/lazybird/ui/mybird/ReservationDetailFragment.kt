package com.limit.lazybird.ui.mybird

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentReservationDetailBinding
import com.limit.lazybird.models.DialogInfo
import com.limit.lazybird.models.retrofit.Exhbt
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.ui.earilybirdDetail.EarlyBirdDetailViewModel
import com.limit.lazybird.ui.earlybirdDetail.EarlyBirdDetailFragment
import com.limit.lazybird.ui.exhibitionDetail.ExhibitionDetailFragment
import com.limit.lazybird.ui.exhibitionDetail.ExhibitionDetailViewModel
import com.limit.lazybird.ui.onboarding.CustomDialogFragment
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
                        showDeleteDialog(exhibitList[position].id)
                    }
                }
            }
        }

        binding.reservationDetailBackBtn.setOnClickListener {
            // 뒤로가기 버튼 클릭
            parentActivity.supportFragmentManager.popBackStack()
        }

        setFragmentResultListener(DeleteExhbtDialogFragment.TAG) { _, bundle ->
            // 정말로 예매를 취소 하시겠습니까? Dialog 결과
            when (bundle.getString(DeleteExhbtDialogFragment.RESULT_CODE)) {
                DeleteExhbtDialogFragment.RESULT_OK -> {
                    val exhbtCd = bundle.getString(DeleteExhbtDialogFragment.EXHBT_CD)!!
                    viewModel.deleteReservationExhibition(exhbtCd)
                    resetView()
                }
            }
        }
    }

    private fun resetView() {
        viewModel.getReservationExhbtList()
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

    private fun showDeleteDialog(exhbtCd: String) {
        val dialogInfo = DialogInfo(
            title = "예매취소",
            message = "정말로 예매를 취소하시겠습니까?",
            positiveBtnTitle = "예매취소",
            negativeBtnTitle = "돌아가기"
        )
        DeleteExhbtDialogFragment().apply {
            // dialog 정보 보내주기
            arguments = bundleOf().apply {
                putParcelable(DeleteExhbtDialogFragment.DIALOG_INFO, dialogInfo)
                putString(DeleteExhbtDialogFragment.EXHBT_CD, exhbtCd)
            }
        }.show(
            parentActivity.supportFragmentManager,
            CustomDialogFragment.TAG
        )
    }
}