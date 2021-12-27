package com.limit.lazybird.ui.earlycard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentEarlycardBinding
import com.limit.lazybird.models.EarlycardInfo
import com.limit.lazybird.ui.BaseFragment
import com.limit.lazybird.viewmodel.EarlycardViewModel
import dagger.hilt.android.AndroidEntryPoint

/************* EarlyCardFragment ***************
 * 메인화면(?? 탭) >> 얼리카드 화면 (Fragment)
 * 얼리카드 정보 리스트로 보기
 ********************************************** ***/
@AndroidEntryPoint
class EarlyCardFragment :
    BaseFragment<FragmentEarlycardBinding>(FragmentEarlycardBinding::inflate) {

    private val viewModel: EarlycardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this

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
    }

    fun clickBackBtn() {
        navController.popBackStack()
    }

    // dialog 보여주기
    private fun showDialog(earlycardInfo: EarlycardInfo) {
        navController.navigate(
            EarlyCardFragmentDirections.actionEarlyCardFragmentToEarlyCardDetailDialogFragment(
                earlycardInfo
            )
        )
    }
}