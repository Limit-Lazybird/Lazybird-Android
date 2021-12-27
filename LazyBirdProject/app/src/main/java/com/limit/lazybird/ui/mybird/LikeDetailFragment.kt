package com.limit.lazybird.ui.mybird

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentLikeDetailBinding
import com.limit.lazybird.models.retrofit.Exhbt
import com.limit.lazybird.ui.BaseFragment
import com.limit.lazybird.ui.MainFragmentDirections
import com.limit.lazybird.viewmodel.MyBirdViewModel
import dagger.hilt.android.AndroidEntryPoint

/************* LikeDetailFragment ***************
 * 메인화면(마이버드 탭) >> 내가 찜한 전시리스트 보기 (Fragment)
 * 내가 찜한 전시리스트 보기
 ********************************************** ***/
@AndroidEntryPoint
class LikeDetailFragment :
    BaseFragment<FragmentLikeDetailBinding>(FragmentLikeDetailBinding::inflate) {

    private val viewModel: MyBirdViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this

        // 좋아요 누른 전시 업데이트 완료
        viewModel.likeExhibitionShortList.observe(viewLifecycleOwner) { exhibitList ->
            if (exhibitList.isEmpty()) {
                binding.likeDetailNoItem.visibility = View.VISIBLE
            } else {
                binding.likeDetailNoItem.visibility = View.INVISIBLE
            }
            binding.likeDetailRecyclerView.adapter = LikeDetailAdapter(exhibitList).apply {
                itemClickListener = object : LikeDetailAdapter.OnItemClickListener {
                    override fun onItemClick(
                        holder: LikeDetailAdapter.ViewHolder,
                        view: View,
                        position: Int
                    ) {
                        // 아이템 클릭 시
                        val exhibitionInfo = viewModel.getLikeExhibitionInfo(position)
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

                    override fun onLikeBtnClick(
                        holder: LikeDetailAdapter.ViewHolder,
                        view: View,
                        position: Int
                    ) {
                        // 하트 클릭 시
                    }
                }
            }
        }
    }

    // 뒤로가기 버튼 클릭 시
    fun clickBackBtn() {
        navController.popBackStack()
    }

    // ExhibitionDetail Fragment 로 이동
    private fun moveToExhibitionDetail(exhibitionInfo: Exhbt) {
        navController.navigate(
            LikeDetailFragmentDirections.actionLikeDetailFragmentToExhibitionDetailFragment(
                exhibitionInfo
            )
        )
    }

    // EarlyBirdDetail Fragment 로 이동
    private fun moveToEarlyBirdDetail(exhibitionInfo: Exhbt) {
        navController.navigate(
            LikeDetailFragmentDirections.actionLikeDetailFragmentToEarlyBirdDetailFragment(
                exhibitionInfo
            )
        )
    }
}