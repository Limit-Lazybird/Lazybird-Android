package com.limit.lazybird.ui.calendar

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.limit.lazybird.R
import com.limit.lazybird.databinding.DialogBsDateSelectBinding
import com.limit.lazybird.databinding.DialogBsUnregisteredListBinding
import com.limit.lazybird.models.CalendarInfoList
import com.limit.lazybird.models.UnregisteredItem
import com.limit.lazybird.ui.calendaradd.DateSelectBSDialog
import com.limit.lazybird.ui.calendaradd.TimeSelectBSDialog
import java.text.SimpleDateFormat

/************* UnregisteredListBSDialog ***************
 * 메인화면(캘린더 탭) >> 전시 일정 추가 (Dialog)
 * 아직 추가되지 않은 전시 일정 중 추가할 전시 선택하는 화면
 ********************************************** ***/
class UnregisteredListBSDialog  : BottomSheetDialogFragment(){

    companion object {
        const val TAG = "UnregisteredListBSDialog"
        const val RESULT_CODE = "unregistered_result_code"
        const val RESULT_OK = "unregistered_result_ok"
        const val RESULT_EXHIBITION = "unregistered_exhibition"
        const val EXHIBITION_LIST = "unregistered_exhibition_list"
        const val SELECTED_POSITION = "unregistered_selected_position"
    }

    private lateinit var binding: DialogBsUnregisteredListBinding
    private lateinit var calendarInfoList: CalendarInfoList

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
        val view = inflater.inflate(R.layout.dialog_bs_unregistered_list, container, false)
        binding = DialogBsUnregisteredListBinding.bind(view)

        arguments?.getParcelable<CalendarInfoList>(EXHIBITION_LIST)?.let {
            calendarInfoList = it
        }

        binding.dialogBsUnregisteredListCloseBtn.setOnClickListener {
            // 취소버튼 클릭 시
            dismiss()
        }
        binding.dialogBsUnregisteredListRecyclerView.adapter = UnregisteredListAdapter(
            calendarInfoList.calendarInfoList.map {
                UnregisteredItem(
                    exhibitionId = it.exhbt_cd,
                    exhibitionName = it.exhbt_nm
                )
            }
        ).apply {
            itemClickListener = object : UnregisteredListAdapter.OnItemClickListener {
                override fun onItemClick(
                    holder: UnregisteredListAdapter.ViewHolder,
                    view: View,
                    position: Int
                ) {
                    // item click 시
                    setFragmentResult(
                        TAG, bundleOf(
                            RESULT_CODE to RESULT_OK,
                            SELECTED_POSITION to position
                        )
                    )
                    dismiss()
                }
            }
        }
        return binding.root
    }

    // 모달창 배경 transparent 으로 지정
    override fun getTheme(): Int = R.style.BottomSheetDialog
}