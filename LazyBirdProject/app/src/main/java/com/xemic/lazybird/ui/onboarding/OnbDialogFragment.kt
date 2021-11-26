package com.xemic.lazybird.ui.onboarding

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.KeyEvent
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.xemic.lazybird.R
import com.xemic.lazybird.custom.OnbSelectBox
import com.xemic.lazybird.models.Survey
import kotlinx.android.synthetic.main.dialog_onb.view.*

/*** deprecated ***/

class OnbDialogFragment : DialogFragment() {

    companion object {
        const val SURVEY = "survey"
        const val PAGE = "page"
        const val SELECT_NO = "selected_no"
        const val TAG = "OnbDialogFragment"
        const val RESULT_CANCLED = "RESULT_CANCLED"
        const val RESULT_MOVE_NEXT = "RESULT_MOVE_NEXT"
        const val RESULT_MOVE_PREV = "RESULT_MOVE_PREV"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layout = layoutInflater.inflate(R.layout.dialog_onb, null).apply {
            val page = requireArguments().getInt(PAGE)
            if (page == 0) {
                // 첫 페이지는 이전문항 확인하는 버튼 없음
                dlg_onb_back.visibility = View.GONE
            }
            val survey = requireArguments().getParcelable<Survey>(SURVEY)!!
            dlg_onb_question.text = survey.question
            for (answerNo in survey.answerList.indices) {
                // weight 가 1 이 걸려있는 LinearLayout 기반 SelectBoxView 를 정답의 개수만큼 넣어주기
                val answer = survey.answerList[answerNo]
                val selectBoxView = OnbSelectBox(context, null).apply {
                    setAnswer(answer.answerTitle)
                    setImage(answer.answerImgUrl)
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0f
                    )
                }
                dlg_onb_answer_container.addView(
                    selectBoxView
                )
            }
            // 모든 선택지에 onClickListener 달아주기
            dlg_onb_answer_container.forEachIndexed { selectedNo, view ->
                val onbSelectBox = view as OnbSelectBox
                onbSelectBox.setOnClickListener {
                    moveNextPage(selectedNo) // 다음페이지로 이동
                }
            }

            dlg_onb_back.setOnClickListener {
                // 이전 문항 이동 버튼
                movePrevPage()
            }

            dlg_onb_close.setOnClickListener {
                // 닫기 버튼
                cancelDialog()
            }
        }

        return AlertDialog.Builder(requireContext(), R.style.DialogThemeFullScreen)
            .setOnKeyListener(object : DialogInterface.OnKeyListener {
                override fun onKey(
                    dialogInterface: DialogInterface?,
                    keyCode: Int,
                    event: KeyEvent
                ): Boolean {
                    // 뒤로가기 눌렀을 때
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        cancelDialog()
                        return true
                    }
                    return false
                }
            })
            .setView(layout)
            .create()
            .apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 하기
                window?.statusBarColor = Color.TRANSPARENT // statusBar 투명하게 하기
            }
    }

    private fun cancelDialog() {
        // dialog 제거
        setFragmentResult(
            TAG, bundleOf(
                "resultCode" to RESULT_CANCLED
            )
        )
        dismiss()
    }

    private fun movePrevPage() {
        setFragmentResult(
            TAG, bundleOf(
                "resultCode" to RESULT_MOVE_PREV
            )
        )
        dismiss()
    }

    private fun moveNextPage(selectedNo: Int) {
        setFragmentResult(
            TAG, bundleOf(
                "resultCode" to RESULT_MOVE_NEXT,
                SELECT_NO to selectedNo
            )
        )
        dismiss()
    }
}