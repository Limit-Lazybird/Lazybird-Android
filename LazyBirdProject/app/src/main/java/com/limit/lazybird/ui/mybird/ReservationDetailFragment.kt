package com.limit.lazybird.ui.mybird

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentReservationDetailBinding
import com.limit.lazybird.models.DialogInfo
import com.limit.lazybird.models.DialogResult
import com.limit.lazybird.models.retrofit.Exhbt
import com.limit.lazybird.ui.BaseFragment
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.ui.MainFragmentDirections
import com.limit.lazybird.ui.custom.dialog.DeleteExhbtDialogFragment
import com.limit.lazybird.ui.custom.dialog.CustomDialogFragment
import com.limit.lazybird.ui.onboarding.OnbFragmentDirections
import com.limit.lazybird.viewmodel.MyBirdViewModel
import dagger.hilt.android.AndroidEntryPoint

/************* ReservationDetailFragment ***************
 * 메인화면(마이버드 탭) >> 내가 예약한 전시리스트 보기 (Fragment)
 * 내가 예약한 전시리스트 보기
 ********************************************** ***/
@AndroidEntryPoint
class ReservationDetailFragment: BaseFragment<FragmentReservationDetailBinding>(FragmentReservationDetailBinding::inflate) {

    private val viewModel: MyBirdViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                        when (exhibitionInfo.eb_yn) {
                            "Y" -> {
                                // 얼리버드 전시 디테일 화면
                                moveToEarlyBirdDetail(exhibitionInfo)
                            }

                            "N" -> {
                                // 일반 전시 디테일 화면
                                moveToExhibitionDetail(exhibitionInfo)
                            }
                        }
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
            clickBackBtn()
        }

        // 정말로 예매를 취소 하시겠습니까? Dialog 결과 반환
        navController.currentBackStackEntry?.savedStateHandle?.apply {
            getLiveData<DialogResult>(DeleteExhbtDialogFragment.TAG)?.observe(viewLifecycleOwner) { dialogResult ->
                when(dialogResult.results[0]){
                    DeleteExhbtDialogFragment.RESULT_OK -> {
                        val exhbtCd = dialogResult.results[1]
                        viewModel.deleteReservationExhibition(exhbtCd)
                        resetView()
                    }
                }
            }
        }
    }

    // 예매한 전시 리스트 재 업데이트
    private fun resetView() {
        viewModel.getReservationExhbtList()
    }

    // 정말로 삭제할 지 dialog 보여주기
    private fun showDeleteDialog(exhbtCd: String) {
        val dialogInfo = DialogInfo(
            title = "예매취소",
            message = "정말로 예매를 취소하시겠습니까?",
            positiveBtnTitle = "예매취소",
            negativeBtnTitle = "돌아가기"
        )
        val action = ReservationDetailFragmentDirections.actionReservationDetailFragmentToDeleteExhbtDialogFragment(dialogInfo, exhbtCd)
        navController.navigate(action)
    }

    // 뒤로가기 버튼 클릭 시
    private fun clickBackBtn() {
        navController.popBackStack()
    }

    // ExhibitionDetail Fragment 로 이동
    private fun moveToExhibitionDetail(exhibitionInfo: Exhbt) {
        navController.navigate(ReservationDetailFragmentDirections.actionReservationDetailFragmentToExhibitionDetailFragment(exhibitionInfo))
    }

    // EarlyBirdDetail Fragment 로 이동
    private fun moveToEarlyBirdDetail(exhibitionInfo: Exhbt) {
        navController.navigate(ReservationDetailFragmentDirections.actionReservationDetailFragmentToEarlyBirdDetailFragment(exhibitionInfo))
    }
}