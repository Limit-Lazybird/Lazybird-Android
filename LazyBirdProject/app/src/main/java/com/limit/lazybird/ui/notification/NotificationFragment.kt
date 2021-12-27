package com.limit.lazybird.ui.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentNotificationBinding
import com.limit.lazybird.ui.BaseFragment
import com.limit.lazybird.ui.custom.SwipeHelperCallback
import com.limit.lazybird.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint

/************** NotificationFragment ****************
 * ?? >> 알림화면 (Fragment)
 * 나에게 온 알림 보기
 ********************************************** ***/
@AndroidEntryPoint
class NotificationFragment :
    BaseFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {

    private val CLAMP_WIDTH_RATIO = 0.2f // CLAMP 의 값이 화면의 가로길이의 몇 퍼센트인지

    private val viewModel: NotificationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this

        // 공지사항 리스트 업데이트
        viewModel.notificationList.observe(viewLifecycleOwner) { notificationList ->
            binding.notificationRecyclerView.adapter = NotificationAdapter(notificationList).apply {
                itemClickListener = object : NotificationAdapter.OnItemClickListener {
                    override fun onDeleteBtnClick(
                        holder: NotificationAdapter.ViewHolder,
                        view: View,
                        position: Int
                    ) {
                        if (holder.itemView.tag?.toString() == "true") {
                            // item 지우기
                            viewModel.deleteNotification(position)
                        }
                    }
                }
            }

            val swipeHelperCallback = SwipeHelperCallback().apply {
                setClamp(binding.notificationRecyclerView.width * CLAMP_WIDTH_RATIO)
            }
            ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(binding.notificationRecyclerView)

            binding.notificationRecyclerView.apply {
                setOnTouchListener { _, _ ->
                    swipeHelperCallback.removePreviousClamp(this)
                    false
                }
            }
        }
    }

    fun clickBackBtn() {
        navController.popBackStack()
    }
}