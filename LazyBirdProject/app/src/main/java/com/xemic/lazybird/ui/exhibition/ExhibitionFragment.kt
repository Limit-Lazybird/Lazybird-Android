package com.xemic.lazybird.ui.exhibition

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.xemic.lazybird.R
import com.xemic.lazybird.custom.OptionItemView
import com.xemic.lazybird.databinding.FragmentExhibitionBinding
import com.xemic.lazybird.models.ExhibitionFilterList
import com.xemic.lazybird.models.retrofit.Exhbt
import com.xemic.lazybird.ui.MainActivity
import com.xemic.lazybird.ui.earilybirdDetail.EarlyBirdDetailViewModel
import com.xemic.lazybird.ui.earlybirdDetail.EarlyBirdDetailFragment
import com.xemic.lazybird.ui.exhibitionDetail.ExhibitionDetailFragment
import com.xemic.lazybird.ui.exhibitionDetail.ExhibitionDetailViewModel
import com.xemic.lazybird.ui.onboarding.OnbFragment
import com.xemic.lazybird.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExhibitionFragment : Fragment(R.layout.fragment_exhibition) {

    private lateinit var binding: FragmentExhibitionBinding
    private val viewModel: ExhibitionViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentExhibitionBinding.bind(view)

        viewModel.exhibitionList.observe(viewLifecycleOwner) { exhibitionList ->
            binding.exhibitionRecyclerView.adapter = ExhibitionAdapter(exhibitionList).apply {
                itemClickListener = object : ExhibitionAdapter.OnItemClickListener {
                    override fun onItemClick(
                        holder: ExhibitionAdapter.ViewHolder,
                        view: View,
                        position: Int
                    ) {
                        // item 클릭 시
                        val exhibitionInfo = viewModel.getExhibitionInfo(position)
                        moveToExhibitionDetail(exhibitionInfo)
                    }

                    override fun onLikeBtnClick(
                        holder: ExhibitionAdapter.ViewHolder,
                        view: View,
                        position: Int
                    ) {
                        // 하트 클릭 시
                        lifecycleScope.launch {
                            viewModel.clickLike(viewModel.getExhibitionInfo(position), holder.isLike)
                            if(binding.exhibitionCustomSwitch.isChecked)
                                viewModel.getCustomExhbtList()
                            else
                                viewModel.getExhbtList()
                        }
                        // Todo : 차후 개선
//                        holder.isLike = !holder.isLike
//                        if (holder.isLike) {
//                            holder.exhibitionLikeBtn.setImageResource(R.drawable.ic_fav_sm_on)
//                        } else {
//                            holder.exhibitionLikeBtn.setImageResource(R.drawable.ic_fav_sm_off)
//                        }
                    }
                }
            }
        }

        viewModel.optionItemList.observe(viewLifecycleOwner) { optionList ->
            for (idx in optionList.indices) {
                val optionName =  optionList[idx]
                binding.exhibitionOptionItemLayout.addView(
                    OptionItemView(requireContext(), viewLifecycleOwner, optionName).apply {
                        setOnClickListener {
                            if(!isSelected){
                                resetSwitch()
                                resetShortFilter()
                                viewModel.getFilterExhbtList("${idx+1}")
                            } else {
                                viewModel.getExhbtList()
                            }
                            isSelected = !isSelected
                        }
                    }
                )
            }
        }

        binding.exhibitionDetailOptionBtn.setOnClickListener {
            // 상세 필터링 버튼 클릭 시
            ExhibitionFilterBSDialog().show(
                parentFragmentManager,
                ExhibitionFilterBSDialog.TAG
            )
        }

        binding.exhibitionCustomResetBtn.setOnClickListener {
            // 맞춤전시 리셋버튼 클릭
            ExhibitionRefreshBSDialog().show(
                parentFragmentManager,
                ExhibitionRefreshBSDialog.TAG
            )
        }

        binding.exhibitionCustomSwitch.setOnCheckedChangeListener { _, isOn ->
            if(isOn){
                resetShortFilter()
                viewModel.getCustomExhbtList()
            } else {
                viewModel.getExhbtList()
            }
        }

        setFragmentResultListener(ExhibitionRefreshBSDialog.TAG) { _, bundle ->
            when(bundle.getString(ExhibitionRefreshBSDialog.RESULT_CODE)){
                ExhibitionRefreshBSDialog.RESULT_OK -> {
                    parentActivity.supportFragmentManager.replaceFragment(OnbFragment())
                }
            }
        }


        setFragmentResultListener(ExhibitionFilterBSDialog.TAG) { _, bundle ->
            when(bundle.getString(ExhibitionFilterBSDialog.RESULT_CODE)){
                ExhibitionFilterBSDialog.RESULT_OK -> {
                    val exhibitionFilterList = bundle.getParcelable<ExhibitionFilterList>(
                        ExhibitionFilterBSDialog.FILTER_LIST
                    )!!
                    resetSwitch()
                    resetShortFilter()
                    if(!(exhibitionFilterList.exhibitionClassSelectedList +
                        exhibitionFilterList.exhibitionEtcSelectedList +
                        exhibitionFilterList.exhibitionPlaceSelectedList).isNullOrEmpty()){
                        viewModel.getFilterExhbtList((
                                exhibitionFilterList.exhibitionClassSelectedList +
                                        exhibitionFilterList.exhibitionEtcSelectedList.map { it+400 } +
                                        exhibitionFilterList.exhibitionPlaceSelectedList.map { it+300 }
                                ).joinToString(",")
                        )
                    }
                }
            }
        }
    }

    private fun resetSwitch() {
        binding.exhibitionCustomSwitch.isChecked = false
    }

    private fun resetShortFilter() {
        binding.exhibitionOptionItemLayout.allViews.forEach { optionView ->
            optionView.isSelected = false
        }
    }

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
}