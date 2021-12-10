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
import com.limit.lazybird.ui.onboarding.CustomDialogFragment
import dagger.hilt.android.AndroidEntryPoint

/************* EarlycardFragment ***************
 * 메인화면(?? 탭) >> 얼리카드 화면 (Fragment)
 * 얼리카드 정보 리스트로 보기
 ********************************************** ***/
@AndroidEntryPoint
class EarlycardFragment : Fragment(R.layout.fragment_earlycard) {

    private lateinit var binding: FragmentEarlycardBinding
    private val viewModel: EarlycardViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEarlycardBinding.bind(view)

        viewModel.earlycardList.observe(viewLifecycleOwner) { earlycardInfoList ->
            binding.earlycardRecyclerView.adapter = EarlycardAdapter(earlycardInfoList).apply {
                itemClickListener = object : EarlycardAdapter.OnItemClickListener {
                    override fun onItemClick(
                        holder: EarlycardAdapter.ViewHolder,
                        view: View,
                        position: Int
                    ) {
                        // 아이템 클릭했을 때
                        showDialog(earlycardInfoList[position])
                    }
                }
            }
        }

        binding.earlycardBackBtn.setOnClickListener {
            // 뒤로가기 버튼 클릭
            parentActivity.supportFragmentManager.popBackStack()
        }
    }

    private fun showDialog(earlycardInfo: EarlycardInfo) {
        EarlycardDetailDialogFragment().apply {
            // dialog 정보 보내주기
            arguments = bundleOf().apply {
                putParcelable(EarlycardDetailDialogFragment.EARLYCARD_INFO, earlycardInfo)
            }
        }.show(
            parentActivity.supportFragmentManager,
            EarlycardDetailDialogFragment.TAG
        )
    }
}