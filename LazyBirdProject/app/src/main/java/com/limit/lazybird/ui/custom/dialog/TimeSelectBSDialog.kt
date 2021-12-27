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
import com.limit.lazybird.databinding.DialogBsTimeSelectBinding

/************* TimeSelectBSDialog ***************
 * 메인화면(캘린더 탭) >> 일정 추가 >> 시간 선택하기 (Dialog)
 * 시간 선택하는 bottom sheet dialog
 ********************************************** ***/
class TimeSelectBSDialog : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "TimeSelectBSDialog"
        const val TIME_TYPE = "time_type"
        const val TYPE_START = "time_start"
        const val TYPE_END = "time_end"
        const val RESULT_CONTEXT = "result_context"
        const val RESULT_CODE = "date_bsd_result_code"
        const val RESULT_OK = "date_bsd_result_ok"
        const val RESULT_DATE = "date_bsd_result_date"
    }

    private lateinit var binding: DialogBsTimeSelectBinding
    private var timeType = ""

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
        val view = inflater.inflate(R.layout.dialog_bs_time_select, container, false)
        binding = DialogBsTimeSelectBinding.bind(view)

        requireArguments().getString(TIME_TYPE)?.let {
            timeType = it
        }

        // 닫기 버튼 클릭 시
        binding.dialogBsTimeSelectCloseBtn.setOnClickListener {
            dismiss()
        }

        // 확인 버튼 클릭 시
        binding.dialogBsTimeSelectOkBtn.setOnClickListener {
            setFragmentResult(
                timeType, bundleOf(
                    RESULT_CODE to RESULT_OK,
                    RESULT_CONTEXT to "${String.format("%02d", binding.dialogBsTimeSelectPicker.hour)}:${String.format("%02d", binding.dialogBsTimeSelectPicker.minute)}"
                )
            )
            dismiss()
        }
        return binding.root
    }

    // 모달창 배경 transparent 으로 지정
    override fun getTheme(): Int = R.style.BottomSheetDialog
}