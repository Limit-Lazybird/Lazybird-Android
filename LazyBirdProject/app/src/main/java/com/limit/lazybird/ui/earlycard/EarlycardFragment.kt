package com.limit.lazybird.ui.earlycard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentEarlycardBinding
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.ui.search.SearchAdapter
import dagger.hilt.android.AndroidEntryPoint

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

                    }
                }
            }
        }

        binding.earlycardBackBtn.setOnClickListener {
            // 뒤로가기 버튼 클릭
            parentActivity.supportFragmentManager.popBackStack()
        }
    }
}