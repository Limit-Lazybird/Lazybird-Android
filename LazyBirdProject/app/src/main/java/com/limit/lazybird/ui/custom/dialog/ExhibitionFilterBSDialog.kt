package com.limit.lazybird.ui.custom.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.allViews
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.limit.lazybird.R
import com.limit.lazybird.ui.custom.OptionItemView
import com.limit.lazybird.databinding.DialogBsFilterExhibitionBinding
import com.limit.lazybird.models.ExhibitionFilterList

/************* ExhibitionFilterBSDialog ***************
 * 메인화면(전시 탭) >> 상세필터 보기 (Dialog)
 * 상세필터링 선택하는 Dialog 화면
 ********************************************** ***/
class ExhibitionFilterBSDialog : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ExhibitionFilterBSDialog"
        const val SELECTED_LIST = "exhibition_filter_selected_list"
        const val FILTER_LIST = "exhibition_filter_list"
        const val RESULT_CODE = "bsd_result_code"
        const val RESULT_OK = "bsd_result_ok"
        const val RESULT_CANCEL = "bsd_result_cancel"
    }

    private lateinit var binding: DialogBsFilterExhibitionBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), theme)
        bottomSheetDialog.setOnShowListener { dialog ->
            // Full 로 다 올라오도록 설정
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from(bottomSheet!!).skipCollapsed = true
            BottomSheetBehavior.from(bottomSheet!!).isHideable = true
        }
        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_bs_filter_exhibition, container, false)
        binding = DialogBsFilterExhibitionBinding.bind(view)

        val selectedArrayList = arguments?.getIntegerArrayList(SELECTED_LIST)?: arrayListOf()

        val exhibitionClassList = listOf(
            "회화",
            "조형",
            "사진",
            "특별전",
            "체험전",
            "아동전시"
        )
        val exhibitionEtcList = listOf(
            "사진 촬영 가능",
            "주차공간 제공",
            "오디오 제공",
            "무료",
            "온라인관람",
            "공휴일 운영",
            "18시 이후 운영"
        )
        val exhibitionPlaceList = listOf(
            "서울",
            "경기・인천",
            "강원",
            "부산・울산・경남",
            "대구・경북",
            "광주・전라",
            "대전・충청・세종",
            "제주"
        )

        var exhibitionClassSelectedList = Array(exhibitionClassList.size) { false }
        var exhibitionEtcSelectedList = Array(exhibitionEtcList.size) { false }
        var exhibitionPlaceSelectedList = Array(exhibitionPlaceList.size) { false }

        exhibitionClassList.forEachIndexed { idx, name ->
            binding.dialogBsFilterClass.addView(
                OptionItemView(view.context, viewLifecycleOwner, name).apply {
                    setOnClickListener {
                        isSelected = !isSelected
                        exhibitionClassSelectedList[idx] = isSelected
                    }
                    if(selectedArrayList.contains(idx+1)){
                        isSelected = true
                        exhibitionClassSelectedList[idx] = isSelected
                    }
                }
            )
        }

        exhibitionEtcList.forEachIndexed { idx, name ->
            binding.dialogBsFilterEtc.addView(
                OptionItemView(view.context, viewLifecycleOwner, name).apply {
                    setOnClickListener {
                        isSelected = !isSelected
                        exhibitionEtcSelectedList[idx] = isSelected
                    }
                    if(selectedArrayList.contains(idx+400+1)){
                        isSelected = true
                        exhibitionEtcSelectedList[idx] = isSelected
                    }
                }
            )
        }

        exhibitionPlaceList.forEachIndexed { idx, name ->
            binding.dialogBsFilterPlace.addView(
                OptionItemView(view.context, viewLifecycleOwner, name).apply {
                    setOnClickListener {
                        isSelected = !isSelected
                        exhibitionPlaceSelectedList[idx] = isSelected
                    }
                    if(selectedArrayList.contains(idx+300+1)){
                        isSelected = true
                        exhibitionPlaceSelectedList[idx] = isSelected
                    }
                }
            )
        }

        binding.dialogBsFilterOk.setOnClickListener {
            // 확인버튼 클릭 시
            setFragmentResult(TAG, Bundle().apply {
                putString(RESULT_CODE, RESULT_OK)
                putParcelable(
                    FILTER_LIST, ExhibitionFilterList(
                    mutableListOf<Int>().apply {
                        exhibitionClassSelectedList.forEachIndexed { idx, isSelected -> if(isSelected) add(idx+1) }
                    },
                    mutableListOf<Int>().apply {
                        exhibitionEtcSelectedList.forEachIndexed { idx, isSelected -> if(isSelected) add(idx+1) }
                    },
                    mutableListOf<Int>().apply {
                        exhibitionPlaceSelectedList.forEachIndexed { idx, isSelected -> if(isSelected) add(idx+1) }
                    }
                ))
            })
            dismiss()
        }

        binding.dialogBsFilterCancel.setOnClickListener {
            // 취소버튼 클릭 시
            setFragmentResult(TAG, Bundle().apply {
                putString(RESULT_CODE, RESULT_CANCEL)
            })
            dismiss()
        }

        binding.dialogBsRefresh.setOnClickListener {
            // 초기화버튼 클릭 시 (선택된 모든 버튼 초기화)
            binding.dialogBsFilterClass.allViews.forEach { optionItemView ->
                optionItemView.isSelected = false
            }
            binding.dialogBsFilterEtc.allViews.forEach { optionItemView ->
                optionItemView.isSelected = false
            }
            binding.dialogBsFilterPlace.allViews.forEach { optionItemView ->
                optionItemView.isSelected = false
            }
            exhibitionClassSelectedList = Array(exhibitionClassList.size) { false }
            exhibitionEtcSelectedList = Array(exhibitionEtcList.size) { false }
            exhibitionPlaceSelectedList = Array(exhibitionPlaceList.size) { false }
        }

        return binding.root
    }

    // 모달창 배경 transparent 으로 지정
    override fun getTheme(): Int = R.style.BottomSheetDialog
}