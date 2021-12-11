package com.limit.lazybird.ui.earlycard

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.limit.lazybird.R
import com.limit.lazybird.databinding.DialogEarlycardDetailBinding
import com.limit.lazybird.models.EarlycardInfo

/************* EarlycardDetailDialogFragment ***************
 * 메인화면(?? 탭) >> 얼리카드 화면 >> 얼리카드 크게보기 (DialogFragment)
 * 얼리카드 정보 크게 보기, 스샷 찍기
 ********************************************** ***/
class EarlycardDetailDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "EarlycardDetailDialogFragment"
        const val EARLYCARD_INFO = "EARLYCARD_INFO"
    }

    private lateinit var binding: DialogEarlycardDetailBinding
    private var alertDialog: AlertDialog? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val earlycardInfo = requireArguments().getParcelable<EarlycardInfo>(EARLYCARD_INFO)!!
        val layout = layoutInflater.inflate(R.layout.dialog_earlycard_detail, null).apply {
            binding = DialogEarlycardDetailBinding.bind(this)
        }

        binding.apply {
            visitedDate = if(earlycardInfo.visitDate!="N") earlycardInfo.visitDate else "-"
            earlycardDetailTitle.text = earlycardInfo.title
            earlycardDetailNumber.text = "No. ${earlycardInfo.no}"
            Glide.with(layout)
                .load(earlycardInfo.imgUrl)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(30)))
                .into(earlycardDetailImg)
            earlycardDetailBackground.setOnClickListener {
                // 배경 클릭
                clickClose()
            }
            earlycardDetailCloseBtn.setOnClickListener {
                // 닫기 버튼 클릭
                clickClose()
            }
            earlycardDetailSaveBtn.setOnClickListener {
                // 스크린샷 버튼 클릭
                clickTicketSave()
            }
        }

        alertDialog = AlertDialog.Builder(
            requireContext(),
            R.style.DialogThemeFullScreen
        ) // fullscreen dialogFragment theme 적용
            .setView(layout)
            .create()
            .apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 하기
                window?.statusBarColor = Color.TRANSPARENT // statusBar 투명하게 하기
            }

        return alertDialog!!
    }

    private fun clickTicketSave() {
        // 티켓이미지 저장 버튼 클릭
        Toast.makeText(requireContext(), "저장이 완료되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun clickClose() {
        // 취소 버튼 클릭 시
        dismiss()
    }
}