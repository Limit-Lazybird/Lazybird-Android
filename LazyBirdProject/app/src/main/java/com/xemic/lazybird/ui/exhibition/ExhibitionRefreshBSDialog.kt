package com.xemic.lazybird.ui.exhibition

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.DialogBsRefreshExhibitionBinding

class ExhibitionRefreshBSDialog : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ExhibitionRefreshBSDialog"
        const val RESULT_CODE = "bsd_refresh_result_code"
        const val RESULT_OK = "bsd_refresh_result_ok"
        const val RESULT_CANCEL = "bsd_refresh_result_cancel"
    }

    lateinit var binding: DialogBsRefreshExhibitionBinding

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
        val view = inflater.inflate(R.layout.dialog_bs_refresh_exhibition, container, false)
        binding = DialogBsRefreshExhibitionBinding.bind(view)

        binding.dialogBsFilterOk.setOnClickListener {
            // 재설정버튼 클릭 시
            val bundle = Bundle()
            bundle.putString(RESULT_CODE, RESULT_OK)
            setFragmentResult(TAG, bundle)
            dismiss()
        }

        binding.dialogBsRefreshClose.setOnClickListener {
            // 취소버튼 클릭 시
            val bundle = Bundle()
            bundle.putString(RESULT_CODE, RESULT_CANCEL)
            setFragmentResult(TAG, bundle)
            dismiss()
        }

        return binding.root
    }

    override fun getTheme(): Int = R.style.BottomSheetDialog // 모달창 배경 transparent 으로 지정
}