package com.limit.lazybird.ui.custom.dialog

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
import com.limit.lazybird.R
import com.limit.lazybird.databinding.DialogBsLogoutBinding

/************* ExhibitionFilterBSDialog ***************
 * 메인화면(마이버드 탭) >> 옵션 >> 로그아웃 (BottomSheetDialogFragment)
 * 정말로 로그아웃 할지 선택하는 화면
 ********************************************** ***/
class LogoutBSDialog : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "LogoutBSDialog"
        const val RESULT_CODE = "bsd_logout_result_code"
        const val RESULT_OK = "bsd_logout_result_ok"
        const val RESULT_CANCEL = "bsd_logout_result_cancel"
    }

    lateinit var binding: DialogBsLogoutBinding

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
        val view = inflater.inflate(R.layout.dialog_bs_logout, container, false)
        binding = DialogBsLogoutBinding.bind(view)

        // 로그아웃 하기 버튼
        binding.dialogBsLogoutOk.setOnClickListener {
            val bundle = Bundle().apply {
                putString(RESULT_CODE, RESULT_OK)
            }
            setFragmentResult(TAG, bundle)
            dismiss()
        }

        // x 버튼
        binding.dialogBsLogoutClose.setOnClickListener {
            dismiss()
        }

        // 로그아웃 취소 버튼
        binding.dialogBsLogoutCancel.setOnClickListener {
            dismiss()
        }
            
        return binding.root
    }

    override fun getTheme(): Int = R.style.BottomSheetDialog // 모달창 배경 transparent 으로 지정
}