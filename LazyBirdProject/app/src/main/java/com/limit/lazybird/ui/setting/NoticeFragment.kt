package com.limit.lazybird.ui.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentNoticeBinding
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.viewmodel.NoticeViewModel
import dagger.hilt.android.AndroidEntryPoint

/***************** NoticeFragment *******************
 * 메인화면(마이버드 탭) >> 옵션 >> 공지사항 (Fragment)
 * 공지사항 전체 보기
 ********************************************** ***/
@AndroidEntryPoint
class NoticeFragment : Fragment(R.layout.fragment_notice) {

    companion object {
        const val TAG = "NoticeFragment"
    }

    lateinit var binding: FragmentNoticeBinding
    private val viewModel: NoticeViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNoticeBinding.bind(view)

        // 뒤로가기 버튼
        binding.noticeBackBtn.setOnClickListener {
            parentActivity.supportFragmentManager.popBackStack()
        }

        // 공지사항 업데이트
        viewModel.noticeInfoList.observe(viewLifecycleOwner) { noticeInfoList ->
            binding.noticeRecyclerView.adapter = NoticeAdapter(noticeInfoList).apply {
                itemClickListener = object : NoticeAdapter.OnItemClickListener {
                    override fun onExpandBtnClick(
                        holder: NoticeAdapter.ViewHolder,
                        view: View,
                        position: Int
                    ) {
                        if(holder.noticeContext.visibility == View.VISIBLE){
                            holder.noticeExpandBtn.setImageResource(R.drawable.ic_expand_light_d)
                            holder.noticeContext.visibility = View.GONE
                        } else {
                            holder.noticeExpandBtn.setImageResource(R.drawable.ic_expand_light_u)
                            holder.noticeContext.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
}