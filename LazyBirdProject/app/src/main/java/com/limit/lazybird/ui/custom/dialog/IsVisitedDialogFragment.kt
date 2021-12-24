package com.limit.lazybird.ui.custom.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.limit.lazybird.R
import com.limit.lazybird.databinding.DialogCustomBinding
import com.limit.lazybird.models.DialogInfo
import android.view.View

/************* CustomDialogFragment ***************
 * 확인/취소 선택버튼을 가진 Custom 모달창 (DialogFragment)
 ********************************************** ***/
class IsVisitedDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "IsVisitedDialogFragment"
        const val DIALOG_INFO = "isvisted_dialog_info"
        const val RESULT_CODE = "isvisited_result_code"
        const val RESULT_CANCEL = "isvisted_result_cancel"
        const val RESULT_OK = "isvisted_result_ok"
        const val EXHBT_CD = "exhbt_cd"
        const val IS_CUSTOM = "is_custom"
    }

    private lateinit var binding: DialogCustomBinding
    private var alertDialog: AlertDialog? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogInfo = requireArguments().getParcelable<DialogInfo>(DIALOG_INFO)!!
        val layout = layoutInflater.inflate(R.layout.dialog_custom, null).apply {
            binding = DialogCustomBinding.bind(this)
        }
        binding.apply {
            onbCancelTitle.text = dialogInfo.title
            onbCancelMessage.text = dialogInfo.message
            onbCancelButtonNo.text = dialogInfo.negativeBtnTitle
            onbCancelButtonYes.text = dialogInfo.positiveBtnTitle
            onbCancelButtonNo.setOnClickListener {
                clickNo()
            }
            onbCancelButtonYes.setOnClickListener {
                clickYes()
            }
            if(dialogInfo.message == ""){
                onbCancelMessage.visibility = View.GONE
            }
        }

        alertDialog = AlertDialog.Builder(requireContext())
            .setView(layout)
            .create().apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 하기
            }
        return alertDialog!!
    }

    // 확인 버튼 클릭 시
    private fun clickYes() {
        setFragmentResult(
            TAG, bundleOf(
                RESULT_CODE to RESULT_OK,
                EXHBT_CD to requireArguments().getString(EXHBT_CD),
                IS_CUSTOM to requireArguments().getBoolean(IS_CUSTOM)
            )
        )
        dismiss()
    }

    // 취소 버튼 클릭 시
    private fun clickNo() {
        setFragmentResult(
            TAG, bundleOf(
                RESULT_CODE to RESULT_CANCEL
            )
        )
        dismiss()
    }
}