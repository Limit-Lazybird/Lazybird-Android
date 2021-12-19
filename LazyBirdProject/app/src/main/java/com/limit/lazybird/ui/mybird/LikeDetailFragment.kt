package com.limit.lazybird.ui.mybird

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentLikeDetailBinding
import com.limit.lazybird.models.retrofit.Exhbt
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.viewmodel.EarlyBirdDetailViewModel
import com.limit.lazybird.ui.earlybird.EarlyBirdDetailFragment
import com.limit.lazybird.ui.exhibition.ExhibitionDetailFragment
import com.limit.lazybird.viewmodel.ExhibitionDetailViewModel
import com.limit.lazybird.util.replaceFragment
import com.limit.lazybird.viewmodel.MyBirdViewModel
import dagger.hilt.android.AndroidEntryPoint

/************* LikeDetailFragment ***************
 * 메인화면(마이버드 탭) >> 내가 찜한 전시리스트 보기 (Fragment)
 * 내가 찜한 전시리스트 보기
 ********************************************** ***/
@AndroidEntryPoint
class LikeDetailFragment :Fragment(R.layout.fragment_like_detail) {

    companion object {
        const val TAG = "LikeDetailFragment"
    }

    lateinit var binding: FragmentLikeDetailBinding
    private val viewModel: MyBirdViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLikeDetailBinding.bind(view)

        viewModel.likeExhibitionShortList.observe(viewLifecycleOwner) { exhibitList ->
            if(exhibitList.isEmpty()){
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
                        moveToExhibitionDetail(exhibitionInfo)
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

        binding.likeDetailBackBtn.setOnClickListener { 
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