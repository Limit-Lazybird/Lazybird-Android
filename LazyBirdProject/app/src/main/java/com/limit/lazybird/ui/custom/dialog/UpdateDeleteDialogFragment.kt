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
import com.limit.lazybird.databinding.DialogUpdateDeleteBinding

/************* UpdateDeleteDialogFragment ***************
 * 수정/삭제 선택버튼을 가진 Custom 모달창 (DialogFragment)
 ********************************************** ***/
class UpdateDeleteDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "UpdateDeleteDialogFragment"
        const val RESULT_CODE = "update_delete_result_code"
        const val RESULT_UPDATE = "upd_del_result_update"
        const val RESULT_DELETE = "upd_del_result_delete"
        const val SCHEDULE_INFO = "upd_del_calendar_info"
    }

    private lateinit var binding: DialogUpdateDeleteBinding
    private var alertDialog: AlertDialog? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layout = layoutInflater.inflate(R.layout.dialog_update_delete, null).apply {
            binding = DialogUpdateDeleteBinding.bind(this)
        }
        binding.apply {
            dialogUpdDelUpdateBtn.setOnClickListener {
                // 수정 버튼 클릭
                clickUpdate()
            }
            dialogUpdDelDeleteBtn.setOnClickListener {
                // 삭제 버튼 클릭
                clickDelete()
            }
        }

        alertDialog = AlertDialog.Builder(requireContext())
            .setView(layout)
            .create().apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 하기
            }
        return alertDialog!!
    }

    private fun clickUpdate() {
        // 확인 버튼 클릭 시
        setFragmentResult(
            TAG, bundleOf(
                RESULT_CODE to RESULT_UPDATE,
                SCHEDULE_INFO to requireArguments().getParcelable(SCHEDULE_INFO)!!
            )
        )
        dismiss()
    }

    private fun clickDelete() {
        // 취소 버튼 클릭 시
        setFragmentResult(
            TAG, bundleOf(
                RESULT_CODE to RESULT_DELETE,
                SCHEDULE_INFO to requireArguments().getParcelable(SCHEDULE_INFO)!!
            )
        )
        dismiss()
    }
}