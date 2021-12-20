package com.limit.lazybird.ui.custom.dialog

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.limit.lazybird.R
import com.limit.lazybird.databinding.DialogEarlycardDetailBinding
import com.limit.lazybird.models.EarlycardInfo
import java.io.File
import java.io.FileOutputStream
import java.util.*

/************* EarlyCardDetailDialogFragment ***************
 * 메인화면(?? 탭) >> 얼리카드 화면 >> 얼리카드 크게보기 (DialogFragment)
 * 얼리카드 정보 크게 보기, 스샷 찍기
 ********************************************** ***/
class EarlyCardDetailDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "EarlyCardDetailDialogFragment"
        const val EARLYCARD_INFO = "EARLYCARD_INFO"
    }

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSION_STORAGE = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private val args: EarlyCardDetailDialogFragmentArgs by navArgs()
    private lateinit var binding: DialogEarlycardDetailBinding
    private var parentActivity: Activity? = null
    private var alertDialog: AlertDialog? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val earlycardInfo = args.dialogInfo
        val layout = layoutInflater.inflate(R.layout.dialog_earlycard_detail, null).apply {
            binding = DialogEarlycardDetailBinding.bind(this)
        }

        binding.apply {
            visitedDate = if(earlycardInfo.visitDate!="N") earlycardInfo.visitDate else "-"
            earlycardDetailTitle.text = earlycardInfo.title
            earlycardDetailNumber.text = "NO. ${earlycardInfo.no}"
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
                if(Build.VERSION.SDK_INT >= 30){
                    clickTicketSave()
                } else {
                    clickTicketSave()
                }
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
        verifyStoragePermission()
        takeScreenShot()
        Toast.makeText(requireContext(), "스크린샷 저장이 완료되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun clickClose() {
        // 취소 버튼 클릭 시
        findNavController().popBackStack()
    }

    fun verifyStoragePermission() {
        val permission =
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                parentActivity!!,
                PERMISSION_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    private fun takeScreenShot() {
        val view = alertDialog!!.window!!.decorView.rootView
        view.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false
        val filePath =
            Environment.getExternalStorageDirectory().path + "/Download/" + Calendar.getInstance().time.time.toString() + ".jpg"
        val fileScreenShot = File(filePath)

        try {
            val fileOutputStream = FileOutputStream(fileScreenShot)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = activity
    }
}