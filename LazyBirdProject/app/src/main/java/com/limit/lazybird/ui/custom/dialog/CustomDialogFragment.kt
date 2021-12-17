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
class CustomDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "CustomDialogFragment"
        const val RESULT_CODE = "result_code"
        const val DIALOG_INFO = "DIALOG_INFO"
        const val RESULT_CANCEL = "result_cancel"
        const val RESULT_OK = "result_ok"
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
                cancelDialog()
            }
            onbCancelButtonYes.setOnClickListener {
                cancelOnb()
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

    private fun cancelOnb() {
        // 확인 버튼 클릭 시
        setFragmentResult(
            TAG, bundleOf(
                RESULT_CODE to RESULT_OK
            )
        )
        dismiss()
    }

    private fun cancelDialog() {
        // 취소 버튼 클릭 시
        setFragmentResult(
            TAG, bundleOf(
                RESULT_CODE to RESULT_CANCEL
            )
        )
        dismiss()
    }
}