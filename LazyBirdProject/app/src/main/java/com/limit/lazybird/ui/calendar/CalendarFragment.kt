package com.limit.lazybird.ui.calendar

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.kizitonwose.calendarview.model.*
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.utils.Size
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import com.limit.lazybird.R
import com.limit.lazybird.ui.custom.DayViewContainer
import com.limit.lazybird.ui.custom.MonthViewContainer
import com.limit.lazybird.ui.custom.ScheduleMarkContainer
import com.limit.lazybird.databinding.FragmentCalendarBinding
import com.limit.lazybird.models.CalendarInfoList
import com.limit.lazybird.models.DialogInfo
import com.limit.lazybird.models.Schedule
import com.limit.lazybird.models.retrofit.CalendarInfo
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.ui.MainFragmentDirections
import com.limit.lazybird.ui.custom.dialog.IsVisitedDialogFragment
import com.limit.lazybird.ui.custom.dialog.UnregisteredListBSDialog
import com.limit.lazybird.ui.custom.dialog.UpdateDeleteDialogFragment
import com.limit.lazybird.util.*
import com.limit.lazybird.viewmodel.CalendarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.*

/**************** CalendarFragment ******************
 * 메인화면(캘린더 탭) (Fragment)
 * 캘린더에서 (예약된 or 예약되지 않은)전시일정정보 확인
 ********************************************** ***/
@AndroidEntryPoint
class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    companion object {
        const val TAG = "CalendarFragment"
    }

    private val DAY_VIEW_HEGIHT = 150 // "일" Container 의 높이

    private lateinit var navController: NavController
    private lateinit var binding: FragmentCalendarBinding
    private val viewModel: CalendarViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    lateinit var unregisteredList: CalendarInfoList // 등록되지 않은 전시 리스트
    lateinit var scheduleMap: Map<Long, MutableList<Schedule>> // 일정 Map

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = requireView().findNavController()
        binding = FragmentCalendarBinding.bind(view)

        // 일정 Map 업데이트
        viewModel.scheduleListDict.observe(viewLifecycleOwner) {
            scheduleMap = it
            calendarViewInit()
            lifecycleScope.launchWhenStarted {
                viewModel.selectedDateLiveData.collect { date ->
                    scheduleLayoutUpdate(date)
                }
            }
        }

        // 등록되지 않은 전시 리스트 업데이트
        viewModel.unregisteredListLiveData.observe(viewLifecycleOwner) { calendarInfoList ->
            unregisteredList = CalendarInfoList(calendarInfoList)
            binding.unregisterCount = calendarInfoList.size
        }

        // 커스텀 일정 추가하기 버튼 클릭
        binding.calendarCustomBtn.setOnClickListener {
            moveToCalendarAdd()
        }

        // 추가되지 않은 전시 일정이 N개 있습니다 버튼 클릭
        binding.calendarSubHeader.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable(UnregisteredListBSDialog.EXHIBITION_LIST, unregisteredList)
            }
            UnregisteredListBSDialog().apply {
                arguments = bundle
            }.show(
                parentFragmentManager,
                UnregisteredListBSDialog.TAG
            )
        }

        // 추가되지 않은 전시 일정 N개 추가하는 BottomSheetDialog 반환 결과
        setFragmentResultListener(UnregisteredListBSDialog.TAG) { _, bundle ->
            when (bundle.getString(UnregisteredListBSDialog.RESULT_CODE)) {
                UnregisteredListBSDialog.RESULT_OK -> {
                    val position = bundle.getInt(UnregisteredListBSDialog.SELECTED_POSITION)
                    moveToCalendarAdd(viewModel.getUnregisteredInfo(position))
                }
            }
        }

        // 캘린더 수정/삭제 dialog 반환 결과
        childFragmentManager.setFragmentResultListener(
            UpdateDeleteDialogFragment.TAG,
            viewLifecycleOwner
        ) { _, bundle ->
            when (bundle.getString(UpdateDeleteDialogFragment.RESULT_CODE)) {
                // 수정 버튼 클릭
                UpdateDeleteDialogFragment.RESULT_UPDATE -> {
                    val schedule: Schedule =
                        bundle.getParcelable(UpdateDeleteDialogFragment.SCHEDULE_INFO)!!
                    if (schedule.isCustom) {
                        moveToCalendarUpdate(schedule, true)
                    } else {
                        moveToCalendarUpdate(schedule, false)
                    }
                }
                // 삭제 버튼 클릭
                UpdateDeleteDialogFragment.RESULT_DELETE -> {
                    val schedule: Schedule =
                        bundle.getParcelable(UpdateDeleteDialogFragment.SCHEDULE_INFO)!!
                    if (schedule.isCustom) {
                        viewModel.deleteCustomCalendarInfo(schedule.id.toString())
                    } else {
                        viewModel.deleteCalendarInfo(schedule.id.toString())
                    }
                }
            }
        }

        // 전시 방문 확인했음 dialog 반환 결과
        childFragmentManager.setFragmentResultListener(
            IsVisitedDialogFragment.TAG,
            viewLifecycleOwner
        ) { _, bundle ->
            when (bundle.getString(IsVisitedDialogFragment.RESULT_CODE)) {
                IsVisitedDialogFragment.RESULT_OK -> {
                    val exhbt_cd = bundle.getString(IsVisitedDialogFragment.EXHBT_CD)
                    val is_custom = bundle.getBoolean(IsVisitedDialogFragment.IS_CUSTOM)
                    if (is_custom && exhbt_cd != null) {
                        viewModel.visitUpdateCustomYes(exhbt_cd)
                    } else if (!is_custom && exhbt_cd != null) {
                        viewModel.visitUpdateExhbtYes(exhbt_cd)
                    }
                }
            }
        }
    }

    // CalendarAddFragment 로 이동 (커스텀 전시 추가하기)
    private fun moveToCalendarAdd() {
        navController.navigate(MainFragmentDirections.actionMainFragmentToCalendarAddFragment(
            CalendarAddFragment.TYPE_CUSTOM,
            true,
            null,
            null
        ))
//        parentActivity.supportFragmentManager.replaceFragment(CalendarAddFragment().apply {
//            arguments = bundleOf(
//                CalendarAddFragment.ADD_TYPE to CalendarAddFragment.TYPE_CUSTOM,
//                CalendarAddFragment.IS_ADD to true,
//                CalendarAddFragment.TICKET_INFO to null
//            )
//        })
    }

    // CalendarAddFragment 로 이동 (미등록 전시 추가하기)
    private fun moveToCalendarAdd(calendarInfo: CalendarInfo) {
        navController.navigate(MainFragmentDirections.actionMainFragmentToCalendarAddFragment(
            CalendarAddFragment.TYPE_TICKETED,
            true,
            calendarInfo,
            null
        ))
//        parentActivity.supportFragmentManager.replaceFragment(CalendarAddFragment().apply {
//            arguments = bundleOf(
//                CalendarAddFragment.ADD_TYPE to CalendarAddFragment.TYPE_TICKETED,
//                CalendarAddFragment.IS_ADD to true,
//                CalendarAddFragment.TICKET_INFO to calendarInfo
//            )
//        })
    }

    // CalendarAddFragment 로 이동 (수정하기)
    private fun moveToCalendarUpdate(schedule: Schedule, isCustom: Boolean) {
        navController.navigate(MainFragmentDirections.actionMainFragmentToCalendarAddFragment(
            CalendarAddFragment.TYPE_TICKETED,
            false,
            null,
            schedule
        ))
//        parentActivity.supportFragmentManager.replaceFragment(CalendarAddFragment().apply {
//            arguments = bundleOf(
//                CalendarAddFragment.ADD_TYPE to if (isCustom) CalendarAddFragment.TYPE_CUSTOM else CalendarAddFragment.TYPE_TICKETED,
//                CalendarAddFragment.IS_ADD to false,
//                CalendarAddFragment.TICKET_INFO to schedule
//            )
//        })
    }

    // 수정하기, 제거하기 선택 Dialog 띄워줌
    private fun showUpdDelDialog(schedule: Schedule) {
        UpdateDeleteDialogFragment().apply {
            arguments = bundleOf().apply {
                putParcelable(UpdateDeleteDialogFragment.SCHEDULE_INFO, schedule)
            }
        }.show(
            childFragmentManager,
            UpdateDeleteDialogFragment.TAG
        )
    }

    // 방문하였는지 확인하는 Dialog 띄워줌
    private fun showDialog(exhbt_cd: String, is_custom: Boolean) {
        val dialogInfo = DialogInfo(
            title = "전시회는 잘 방문하셨나요?",
            message = "방문 확인을 하시면 캘린더에 표시됩니다.",
            positiveBtnTitle = "다녀왔어요",
            negativeBtnTitle = "아니오"
        )
        IsVisitedDialogFragment().apply {
            // dialog 정보 보내주기
            arguments = bundleOf().apply {
                putParcelable(IsVisitedDialogFragment.DIALOG_INFO, dialogInfo)
                putString(IsVisitedDialogFragment.EXHBT_CD, exhbt_cd)
                putBoolean(IsVisitedDialogFragment.IS_CUSTOM, is_custom)
            }
        }.show(
            childFragmentManager,
            IsVisitedDialogFragment.TAG
        )
    }

    // 캘린더 화면 초기화
    private fun calendarViewInit() {
        // 세로 크기 조절
        binding.calendarView.daySize = Size(
            binding.calendarView.daySize.width,
            DAY_VIEW_HEGIHT
        )

        // "일" view binding
        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View): DayViewContainer = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                if (day.owner == DayOwner.THIS_MONTH) {
                    // 이번달
                    // 오늘 날짜 인 경우
                    if (day.date == LocalDate.now()) {
                        container.isToday.visibility = View.VISIBLE // 오늘인 경우
                    } else {
                        container.isToday.visibility = View.INVISIBLE // 오늘이 아닌 경우
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
                    viewModel.selectDay(container, day) // 날짜를 클릭 했을 때
                }

                container.scheduleMark.removeAllViews()
                if (day.owner == DayOwner.THIS_MONTH
                    && scheduleMap[day.toDate().time]?.size ?: 0 > 0
                ) {
                    for (schedule in scheduleMap[day.toDate().time]!!) {
                        container.scheduleMark.addView(
                            ScheduleMarkContainer(container.view.context, null).apply {
                                if (schedule.isVisited)
                                    setColor(resources.getColorStateList(R.color.green, null))
                                else
                                    setColor(resources.getColorStateList(R.color.gray04, null))
                            }
                        )
                    }
                }
            }
        }

        // "월" view binding
        binding.calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View): MonthViewContainer = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                    container.headerMonth.text = "${month.month}월"
                    container.headerYear.text = "${month.year}"
                    container.nextMonthBtn.setOnClickListener {
                        // 다음 달 버튼 클릭
                        binding.calendarView.findFirstVisibleMonth()?.let {
                            binding.calendarView.smoothScrollToMonth(it.yearMonth.next)
                        }
                    }
                    container.prevMonthBtn.setOnClickListener {
                        // 이전 달 버튼 클릭
                        binding.calendarView.findFirstVisibleMonth()?.let {
                            binding.calendarView.smoothScrollToMonth(it.yearMonth.previous)
                        }
                    }
                }
            }

        // setup
        val currentMonth = YearMonth.now() // 현재 월
        binding.calendarView.setup(
            currentMonth.minusMonths(10),
            currentMonth.plusMonths(10),
            viewModel.firstDayOfWeek
        )
        binding.calendarView.scrollToMonth(currentMonth)
    }

    // selectedDay 에 해당하는 일정내용을 업데이트
    private fun scheduleLayoutUpdate(selectedDay: Date) {
        if (this::scheduleMap.isInitialized
            && scheduleMap.containsKey(selectedDay.time)
            && scheduleMap[selectedDay.time]!!.isNotEmpty()
        ) {
            // schedule 이 존재하고, 일정이 1개 이상 일 때
            binding.calendarScheduleRecyclerView.visibility = View.VISIBLE
            binding.calendarScheduleRecyclerView.adapter =
                CalendarScheduleAdapter(scheduleMap[selectedDay.time]!!).apply {
                    itemClickListener = object : CalendarScheduleAdapter.OnItemClickListener {
                        override fun onIsVisitedClick(
                            holder: CalendarScheduleAdapter.ViewHolder,
                            view: View,
                            position: Int
                        ) {
                            // 방문했음 버튼 클릭
                            with(scheduleMap[selectedDay.time]!![position]) {
                                if (!isVisited) {
                                    showDialog(
                                        exhbt_cd = id.toString(),
                                        is_custom = isCustom
                                    )
                                } else {
                                    if (isCustom) {
                                        viewModel.visitUpdateCustomNo(
                                            exhbt_cd = id.toString()
                                        )
                                    } else {
                                        viewModel.visitUpdateExhbtNo(
                                            exhbt_cd = id.toString()
                                        )
                                    }
                                }
                            }
                        }

                        // 아이템 클릭
                        override fun onItemClick(
                            holder: CalendarScheduleAdapter.ViewHolder,
                            view: View,
                            position: Int
                        ) {
                            showUpdDelDialog(scheduleMap[selectedDay.time]!![position])
                        }
                    }
                }
        } else {
            // schedule 이 존재하지 않음 일 때
            binding.calendarScheduleRecyclerView.visibility = View.GONE
        }
    }
}