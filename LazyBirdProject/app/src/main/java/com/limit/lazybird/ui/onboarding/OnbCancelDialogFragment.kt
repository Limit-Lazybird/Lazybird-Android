package com.limit.lazybird.ui.onboarding

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.limit.lazybird.R
import com.limit.lazybird.databinding.DialogOnbCancelBinding
import com.limit.lazybird.models.DialogInfo

/************* OnbCancelDialogFragment ***************
 * 온보딩 화면 >> 온보딩 취소하기 Dialog (DialogFragment)
 * 진행중인 온보딩을 취소할지에 대한 Dialog
 ********************************************** ***/
class OnbCancelDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "OnbCancelDialogFragment"
        const val DIALOG_INFO = "DIALOG_INFO"
        const val RESULT_CANCEL = "result_cancel"
        const val RESULT_OK = "result_ok"
    }

    private lateinit var binding: DialogOnbCancelBinding
    private var alertDialog: AlertDialog? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogInfo = requireArguments().getParcelable<DialogInfo>(DIALOG_INFO)!!
        val layout = layoutInflater.inflate(R.layout.dialog_onb_cancel, null).apply {
            binding = DialogOnbCancelBinding.bind(this)
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
                "resultCode" to RESULT_OK
            )
        )
        dismiss()
    }

    private fun cancelDialog() {
        // 취소 버튼 클릭 시
        setFragmentResult(
            TAG, bundleOf(
                "resultCode" to RESULT_CANCEL
            )
        )
        dismiss()
    }
}