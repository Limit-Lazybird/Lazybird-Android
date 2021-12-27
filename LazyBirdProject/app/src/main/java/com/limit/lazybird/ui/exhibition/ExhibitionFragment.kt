package com.limit.lazybird.ui.exhibition

import android.os.Bundle
import android.view.View
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.limit.lazybird.R
import com.limit.lazybird.ui.custom.OptionItemView
import com.limit.lazybird.databinding.FragmentExhibitionBinding
import com.limit.lazybird.models.ExhibitionFilterList
import com.limit.lazybird.models.retrofit.Exhbt
import com.limit.lazybird.ui.BaseFragment
import com.limit.lazybird.ui.MainFragmentDirections
import com.limit.lazybird.ui.custom.dialog.ExhibitionFilterBSDialog
import com.limit.lazybird.ui.custom.dialog.ExhibitionRefreshBSDialog
import com.limit.lazybird.viewmodel.ExhibitionViewModel
import dagger.hilt.android.AndroidEntryPoint

/************* ExhibitionFragment ***************
 * 메인화면(전시 탭) (Fragment)
 * 전시 정보 전체 보기
 * Todo : 하트 클릭 정보 Local로 두고, 하트 클릭 시 전시정보 업데이트 안하도록 변경
 * Todo : RecyclerView에 LiveData와 ListAdapter 적용하여, 데이터 변환시 시각적으로 변화하도록 수정
 ********************************************** ***/
@AndroidEntryPoint
class ExhibitionFragment :
    BaseFragment<FragmentExhibitionBinding>(FragmentExhibitionBinding::inflate) {

    private val viewModel: ExhibitionViewModel by viewModels()

    // detailFilter 어떤 것 선택했는지 가지고 있는 리스트
    private var currentDetailFilterSelectedList: List<Int> = listOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this

        // RecyclerView 업데이트
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
                        holder: ExhibitionAdapter.ViewHolder,
                        view: View,
                        position: Int
                    ) {
                        // 하트 클릭 시
                        viewModel.clickLike(viewModel.getExhibitionInfo(position), holder.isLike)
                        if (binding.exhibitionCustomSwitch.isChecked)
                            viewModel.getCustomExhbtList()
                        else
                            viewModel.getExhbtList()
                    }
                }
            }
        }

        // 상단 빠른 필터링 옵션버튼 그려주기
        viewModel.optionItemList.observe(viewLifecycleOwner) { optionList ->
            for (idx in optionList.indices) {
                val optionName = optionList[idx]
                binding.exhibitionOptionItemLayout.addView(
                    OptionItemView(requireContext(), viewLifecycleOwner, optionName).apply {
                        setOnClickListener {
                            if (!isSelected) {
                                resetSwitch()
                                resetShortFilter()
                                resetDetailSelectedList()
                                viewModel.getFilterExhbtList("${idx + 1}")
                            } else {
                                viewModel.getExhbtList()
                            }
                            isSelected = !isSelected
                        }
                    }
                )
            }
        }

        // 스위치 클릭
        binding.exhibitionCustomSwitch.setOnCheckedChangeListener { _, isOn ->
            if (isOn) {
                resetShortFilter()
                resetDetailSelectedList()
                viewModel.getCustomExhbtList()
            } else {
                viewModel.getExhbtList()
            }
        }

        // 전시성향분석 재설정 Dialog 선택 결과 확인
        setFragmentResultListener(ExhibitionRefreshBSDialog.TAG) { _, bundle ->
            when (bundle.getString(ExhibitionRefreshBSDialog.RESULT_CODE)) {
                ExhibitionRefreshBSDialog.RESULT_OK -> {
                    moveToOnb()
                }
            }
        }

        // 상세필터 결과 확인
        setFragmentResultListener(ExhibitionFilterBSDialog.TAG) { _, bundle ->
            when (bundle.getString(ExhibitionFilterBSDialog.RESULT_CODE)) {
                ExhibitionFilterBSDialog.RESULT_OK -> {
                    val exhibitionFilterList = bundle.getParcelable<ExhibitionFilterList>(
                        ExhibitionFilterBSDialog.FILTER_LIST
                    )!!
                    resetSwitch()
                    resetShortFilter()
                    if (!(exhibitionFilterList.exhibitionClassSelectedList +
                                exhibitionFilterList.exhibitionEtcSelectedList +
                                exhibitionFilterList.exhibitionPlaceSelectedList).isNullOrEmpty()
                    ) {
                        currentDetailFilterSelectedList =
                            exhibitionFilterList.exhibitionClassSelectedList +
                                    exhibitionFilterList.exhibitionEtcSelectedList.map { it + 400 } +
                                    exhibitionFilterList.exhibitionPlaceSelectedList.map { it + 300 }
                        viewModel.getFilterExhbtList(currentDetailFilterSelectedList.joinToString(","))
                    } else {
                        // 아무것도 선택 안했을 경우, 전체 검색
                        resetDetailSelectedList()
                        viewModel.getExhbtList()
                    }
                }
            }
        }
    }

    // 전시 분석 재설정하기 버튼 클릭
    fun clickCustomResetBtn() {
        ExhibitionRefreshBSDialog().show(
            parentFragmentManager,
            ExhibitionRefreshBSDialog.TAG
        )
    }

    // 상세 옵션 버튼 클릭
    fun clickDetailOptionBtn() {
        val bundle = Bundle().apply {
            putIntegerArrayList(
                ExhibitionFilterBSDialog.SELECTED_LIST,
                currentDetailFilterSelectedList.toCollection(ArrayList())
            )
        }
        ExhibitionFilterBSDialog().apply {
            arguments = bundle
        }.show(
            parentFragmentManager,
            ExhibitionFilterBSDialog.TAG
        )
    }


    // 현재 선택된 detail filter 목록 초기화
    private fun resetDetailSelectedList() {
        currentDetailFilterSelectedList = listOf()
    }

    // 스위치 초기화 하기
    private fun resetSwitch() {
        binding.exhibitionCustomSwitch.isChecked = false
    }

    // 빠른 필터링 옵션버튼 초기화 하기
    private fun resetShortFilter() {
        binding.exhibitionOptionItemLayout.allViews.forEach { optionView ->
            optionView.isSelected = false
        }
    }

    // EarlyCard Fragment 로 이동
    fun moveToEarlyCard() {
        navController.navigate(MainFragmentDirections.actionMainFragmentToEarlyCardFragment())
    }

    // ExhibitionDetail Fragment 로 이동
    private fun moveToExhibitionDetail(exhibitionInfo: Exhbt) {
        navController.navigate(
            MainFragmentDirections.actionMainFragmentToExhibitionDetailFragment(
                exhibitionInfo
            )
        )
    }

    // EarlyBirdDetail Fragment 로 이동
    private fun moveToEarlyBirdDetail(exhibitionInfo: Exhbt) {
        navController.navigate(
            MainFragmentDirections.actionMainFragmentToEarlyBirdDetailFragment(
                exhibitionInfo
            )
        )
    }

    // Onb Fragment 로 이동
    private fun moveToOnb() {
        navController.navigate(MainFragmentDirections.actionMainFragmentToOnbFragment())
    }

}