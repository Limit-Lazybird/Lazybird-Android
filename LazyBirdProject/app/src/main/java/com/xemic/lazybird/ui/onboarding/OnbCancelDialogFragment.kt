package com.xemic.lazybird.ui.onboarding

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.DialogOnbCancelBinding
import com.xemic.lazybird.models.DialogInfo

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
//            Log.e("test",TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, resources.displayMetrics).toString() )
//            layoutParams = ViewGroup.LayoutParams(
//                100,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
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
        setFragmentResult(
            TAG, bundleOf(
                "resultCode" to RESULT_OK
            )
        )
        dismiss()
    }

    private fun cancelDialog() {
        setFragmentResult(
            TAG, bundleOf(
                "resultCode" to RESULT_CANCEL
            )
        )
        dismiss()
    }
}