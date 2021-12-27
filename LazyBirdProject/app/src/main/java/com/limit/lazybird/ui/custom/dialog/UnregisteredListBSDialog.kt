package com.limit.lazybird.ui.custom.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.limit.lazybird.R
import com.limit.lazybird.databinding.DialogBsUnregisteredListBinding
import com.limit.lazybird.models.CalendarInfoList

/************* UnregisteredListBSDialog ***************
 * 메인화면(캘린더 탭) >> 전시 일정 추가 (Dialog)
 * 아직 추가되지 않은 전시 일정 중 추가할 전시 선택하는 화면
 ********************************************** ***/
class UnregisteredListBSDialog  : BottomSheetDialogFragment(){

    companion object {
        const val TAG = "UnregisteredListBSDialog"
        const val RESULT_CODE = "unregistered_result_code"
        const val RESULT_OK = "unregistered_result_ok"
        const val RESULT_EXHIBITION = "unregistered_exhibition"
        const val EXHIBITION_LIST = "unregistered_exhibition_list"
        const val SELECTED_POSITION = "unregistered_selected_position"
    }

    private lateinit var binding: DialogBsUnregisteredListBinding
    private lateinit var calendarInfoList: CalendarInfoList

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
        val view = inflater.inflate(R.layout.dialog_bs_unregistered_list, container, false)
        binding = DialogBsUnregisteredListBinding.bind(view)

        arguments?.getParcelable<CalendarInfoList>(EXHIBITION_LIST)?.let {
            calendarInfoList = it
        }

        // 취소버튼 클릭 시
        binding.dialogBsUnregisteredListCloseBtn.setOnClickListener {
            dismiss()
        }

        // picker update
        binding.dialogBsUnregisteredListPicker.apply {
            val nameArray:Array<String> = calendarInfoList.calendarInfoList.map {
                it.exhbt_nm
            }.toTypedArray()
            displayedValues = nameArray
            minValue = 0
            maxValue = nameArray.size-1
            wrapSelectorWheel = false // 반복 제거
        }

        // 확인버튼 클릭 시
        binding.dialogBsUnregisteredOkBtn.setOnClickListener {
            setFragmentResult(
                TAG, bundleOf(
                    RESULT_CODE to RESULT_OK,
                    SELECTED_POSITION to binding.dialogBsUnregisteredListPicker.value
                )
            )
            dismiss()
        }

        return binding.root
    }

    // 모달창 배경 transparent 으로 지정
    override fun getTheme(): Int = R.style.BottomSheetDialog
}