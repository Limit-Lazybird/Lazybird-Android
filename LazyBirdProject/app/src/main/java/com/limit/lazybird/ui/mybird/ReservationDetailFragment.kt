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
import com.limit.lazybird.ui.custom.dialog.DeleteExhbtDialogFragment
import com.limit.lazybird.viewmodel.EarlyBirdDetailViewModel
import com.limit.lazybird.ui.earlybird.EarlyBirdDetailFragment
import com.limit.lazybird.ui.exhibition.ExhibitionDetailFragment
import com.limit.lazybird.viewmodel.ExhibitionDetailViewModel
import com.limit.lazybird.ui.custom.dialog.CustomDialogFragment
import com.limit.lazybird.util.replaceFragment
import com.limit.lazybird.viewmodel.MyBirdViewModel
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

        // 예매한 전시 리스트 업데이트
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

        // 뒤로가기 버튼 클릭
        binding.reservationDetailBackBtn.setOnClickListener {
            parentActivity.supportFragmentManager.popBackStack()
        }

        // 정말로 예매를 취소 하시겠습니까? Dialog 결과 반환
        setFragmentResultListener(DeleteExhbtDialogFragment.TAG) { _, bundle ->
            when (bundle.getString(DeleteExhbtDialogFragment.RESULT_CODE)) {
                DeleteExhbtDialogFragment.RESULT_OK -> {
                    val exhbtCd = bundle.getString(DeleteExhbtDialogFragment.EXHBT_CD)!!
                    viewModel.deleteReservationExhibition(exhbtCd)
                    resetView()
                }
            }
        }
    }

    // 예매한 전시 리스트 재 업데이트
    private fun resetView() {
        viewModel.getReservationExhbtList()
    }

    // ExhibitionDetail 화면으로 이동
    private fun moveToExhibitionDetail(exhibitionInfo: Exhbt) {
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

    // 정말로 삭제할 지 dialog 보여주기
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