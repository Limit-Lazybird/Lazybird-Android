package com.limit.lazybird.ui.earlycard

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentEarlycardBinding
import com.limit.lazybird.models.EarlycardInfo
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.ui.custom.dialog.EarlycardDetailDialogFragment
import com.limit.lazybird.viewmodel.EarlycardViewModel
import dagger.hilt.android.AndroidEntryPoint

/************* EarlyCardFragment ***************
 * 메인화면(?? 탭) >> 얼리카드 화면 (Fragment)
 * 얼리카드 정보 리스트로 보기
 ********************************************** ***/
@AndroidEntryPoint
class EarlyCardFragment : Fragment(R.layout.fragment_earlycard) {

    private lateinit var binding: FragmentEarlycardBinding
    private val viewModel: EarlycardViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEarlycardBinding.bind(view)

        viewModel.earlycardList.observe(viewLifecycleOwner) { earlycardInfoList ->
            binding.earlycardRecyclerView.adapter = EarlyCardAdapter(earlycardInfoList).apply {
                itemClickListener = object : EarlyCardAdapter.OnItemClickListener {
                    override fun onItemClick(
                        holder: EarlyCardAdapter.ViewHolder,
                        view: View,
                        position: Int
                    ) {
                        // 아이템 클릭했을 때
                        showDialog(earlycardInfoList[position])
                    }
                }
            }
        }

        // 뒤로가기 버튼 클릭
        binding.earlycardBackBtn.setOnClickListener {
            parentActivity.supportFragmentManager.popBackStack()
        }
    }

    // dialog 보여주기
    private fun showDialog(earlycardInfo: EarlycardInfo) {
        EarlycardDetailDialogFragment().apply {
            arguments = bundleOf().apply {
                putParcelable(EarlycardDetailDialogFragment.EARLYCARD_INFO, earlycardInfo)
            }
        }.show(
            parentActivity.supportFragmentManager,
            EarlycardDetailDialogFragment.TAG
        )
    }
}