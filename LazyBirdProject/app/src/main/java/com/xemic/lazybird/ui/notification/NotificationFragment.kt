package com.xemic.lazybird.ui.notification

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.FragmentNotificationBinding
import com.xemic.lazybird.ui.MainActivity
import com.xemic.lazybird.util.toDp
import dagger.hilt.android.AndroidEntryPoint

/************** NotificationFragment ****************
 * ?? >> 알림화면 (Fragment)
 * 나에게 온 알림 보기
 ********************************************** ***/
@AndroidEntryPoint
class NotificationFragment: Fragment(R.layout.fragment_notification) {

    companion object {
        const val TAG = "NotificationFragment"
    }

    lateinit var binding: FragmentNotificationBinding
    private val viewModel: NotificationViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificationBinding.bind(view)
        binding.notificationBackBtn.setOnClickListener {
            // 뒤로가기 버튼
            parentActivity.supportFragmentManager.popBackStack()
        }
        viewModel.notificationList.observe(viewLifecycleOwner) { notificationList ->
            binding.notificationRecyclerView.adapter = NotificationAdapter(notificationList).apply {
                itemClickListener = object : NotificationAdapter.OnItemClickListener {
                    override fun onDeleteBtnClick(
                        holder: NotificationAdapter.ViewHolder,
                        view: View,
                        position: Int
                    ) {
                        if(holder.itemView.tag?.toString() == "true") {
                            // item 지우기
                        }
                    }
                }
            }
            val swipeHelperCallback = SwipeHelperCallback().apply { setClamp(200f) }
            ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(binding.notificationRecyclerView)

            binding.notificationRecyclerView.apply {
                setOnTouchListener { _, _ ->
                    swipeHelperCallback.removePreviousClamp(this)
                    false
                }
            }
        }
    }
}