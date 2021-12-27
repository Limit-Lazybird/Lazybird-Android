package com.limit.lazybird.ui.custom.dialog

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.utils.Size
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import com.limit.lazybird.R
import com.limit.lazybird.ui.custom.DayViewContainer
import com.limit.lazybird.ui.custom.MonthViewContainer
import com.limit.lazybird.databinding.DialogBsDateSelectBinding
import com.limit.lazybird.util.toDate
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

/************* DateSelectBSDialog ***************
 * 메인화면(캘린더 탭) >> 일정 추가 >> 날짜 선택하기 (Dialog)
 * 날짜 선택하는 bottom sheet dialog
 ********************************************** ***/
class DateSelectBSDialog : BottomSheetDialogFragment(){

    companion object {
        const val TAG = "DateSelectBSDialog"
        const val RESULT_CODE = "date_bsd_result_code"
        const val RESULT_OK = "date_bsd_result_ok"
        const val RESULT_DATE = "date_bsd_result_date"
    }

    private val DAY_VIEW_HEGIHT = 120

    private lateinit var binding: DialogBsDateSelectBinding

    private val _selectedDateLiveData = MutableLiveData<Date>()
    private val selectedDateLiveData: LiveData<Date> get() = _selectedDateLiveData
    private var selectedDayViewContainer: DayViewContainer? = null // 선택된 날짜의 DayViewContainer

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
        val view = inflater.inflate(R.layout.dialog_bs_date_select, container, false)
        binding = DialogBsDateSelectBinding.bind(view)
        calendarViewInit()

        // 취소버튼 클릭 시
        binding.dialogBsDateSelectCloseBtn.setOnClickListener {
            dismiss()
        }

        // 선택하기 버튼 클릭
        binding.dialogBsDateSelectOkBtn.setOnClickListener {
            val selectedDate = selectedDateLiveData.value?.let { mDate ->
                SimpleDateFormat("yyyy-MM-dd").format(mDate)
            } ?: ""

            setFragmentResult(TAG, Bundle().apply {
                putString(RESULT_CODE, RESULT_OK)
                putString(RESULT_DATE, selectedDate)
            })
            dismiss()
        }
        return binding.root
    }

    private fun calendarViewInit() {
        binding.dialogBsDateSelectCalendar.daySize = Size(
            binding.dialogBsDateSelectCalendar.daySize.width,
            DAY_VIEW_HEGIHT
        )

        binding.dialogBsDateSelectCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View): DayViewContainer = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                if (day.owner == DayOwner.THIS_MONTH) {
                    // 선택된 날짜 인 경우
                    if (day.toDate() == selectedDateLiveData.value) {
                        container.isSelected.visibility = View.VISIBLE // 선택 표시 생성
                        selectedDayViewContainer = container // container 업데이트
                    }

                    // 요일별 구분
                    when (day.date.dayOfWeek) {
                        DayOfWeek.SATURDAY ->
                            container.textView.setTextColor(Color.WHITE) // 토요일
                        DayOfWeek.SUNDAY ->
                            container.textView.setTextColor(Color.WHITE) // 일요일
                        else ->
                            container.textView.setTextColor(Color.WHITE) // 평일
                    }
                } else {
                    container.textView.setTextColor(Color.TRANSPARENT) // 이전달, 다음달 글씨 색
                }
                container.textView.text = day.date.dayOfMonth.toString()
                container.textView.setOnClickListener {
                    selectDay(container, day) // 날짜를 클릭 했을 때
                }
            }
        }

        binding.dialogBsDateSelectCalendar.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View): MonthViewContainer = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                    container.headerMonth.text = "${month.month}월"
                    container.headerYear.text = "${month.year}"
                    container.nextMonthBtn.setOnClickListener {
                        // 다음 달 버튼 클릭
                        binding.dialogBsDateSelectCalendar.findFirstVisibleMonth()?.let {
                            binding.dialogBsDateSelectCalendar.smoothScrollToMonth(it.yearMonth.next)
                        }
                    }
                    container.prevMonthBtn.setOnClickListener {
                        // 이전 달 버튼 클릭
                        binding.dialogBsDateSelectCalendar.findFirstVisibleMonth()?.let {
                            binding.dialogBsDateSelectCalendar.smoothScrollToMonth(it.yearMonth.previous)
                        }
                    }
                }
            }

        val currentMonth = YearMonth.now()
        binding.dialogBsDateSelectCalendar.setup(
            currentMonth.minusMonths(10),
            currentMonth.plusMonths(10),
            WeekFields.of(Locale.getDefault()).firstDayOfWeek
        )
        binding.dialogBsDateSelectCalendar.scrollToMonth(currentMonth)
    }

    fun selectDay(container: DayViewContainer, day: CalendarDay) {
        selectedDayViewContainer?.isSelected?.visibility = View.INVISIBLE // 기존 viewContainer 선택 표시 제거
        selectedDayViewContainer = container // 선택한 viewContainer 로 저장
        container.isSelected.visibility = View.VISIBLE // viewContainer 선택 표시 생성
        _selectedDateLiveData.postValue( // 선택된 날짜 업데이트
            day.toDate()
        )
    }

    // 모달창 배경 transparent 으로 지정
    override fun getTheme(): Int = R.style.BottomSheetDialog
}