package com.xemic.lazybird.ui.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.FragmentNoticeBinding
import com.xemic.lazybird.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoticeFragment : Fragment(R.layout.fragment_notice) {

    lateinit var binding: FragmentNoticeBinding
    private val viewModel: NoticeViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNoticeBinding.bind(view)
        binding.noticeBackBtn.setOnClickListener {
            // 뒤로가기 버튼
            parentActivity.supportFragmentManager.popBackStack()
        }
        viewModel.noticeList.observe(viewLifecycleOwner) { noticeInfoList ->
            binding.noticeRecyclerView.adapter = NoticeAdapter(noticeInfoList).apply {
                itemClickListener = object : NoticeAdapter.OnItemClickListener {
                    override fun onExpandBtnClick(
                        holder: NoticeAdapter.ViewHolder,
                        view: View,
                        position: Int
                    ) {
                        if(holder.noticeContext.visibility == View.VISIBLE){
                            holder.noticeContext.visibility = View.GONE
                        } else {
                            holder.noticeContext.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
}