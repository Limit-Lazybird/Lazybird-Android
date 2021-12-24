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

/************* DeleteExhbtDialogFragment ***************
 * 메인화면(마이버드 탭) >> 내가 예약한 전시리스트 보기 >> 삭제하기 (DialogFragment)
 * 예약한 전시 취소하는  Custom 모달창
 ********************************************** ***/
class DeleteExhbtDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "DeleteExhbtDialogFragment"
        const val RESULT_CODE = "delete_exhbt_result_code"
        const val DIALOG_INFO = "delete_exhbt_DIALOG_INFO"
        const val RESULT_CANCEL = "delete_exhbt_result_cancel"
        const val RESULT_OK = "delete_exhbt_result_ok"
        const val EXHBT_CD = "delete_exhbt_exhbt_cd"
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

    // 확인 버튼 클릭 시
    private fun cancelOnb() {
        setFragmentResult(
            TAG, bundleOf(
                RESULT_CODE to RESULT_OK,
                EXHBT_CD to requireArguments().getString(EXHBT_CD)!!
            )
        )
        dismiss()
    }

    // 취소 버튼 클릭 시
    private fun cancelDialog() {
        setFragmentResult(
            TAG, bundleOf(
                RESULT_CODE to RESULT_CANCEL
            )
        )
        dismiss()
    }
}