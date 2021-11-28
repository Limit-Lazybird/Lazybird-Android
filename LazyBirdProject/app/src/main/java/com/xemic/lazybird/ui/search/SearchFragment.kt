package com.xemic.lazybird.ui.search

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.xemic.lazybird.R
import com.xemic.lazybird.custom.OptionItemView
import com.xemic.lazybird.databinding.FragmentSearchBinding
import com.xemic.lazybird.models.retrofit.Exhbt
import com.xemic.lazybird.ui.MainActivity
import com.xemic.lazybird.ui.earilybirdDetail.EarlyBirdDetailViewModel
import com.xemic.lazybird.ui.earlybirdDetail.EarlyBirdDetailFragment
import com.xemic.lazybird.ui.exhibitionDetail.ExhibitionDetailFragment
import com.xemic.lazybird.ui.exhibitionDetail.ExhibitionDetailViewModel
import com.xemic.lazybird.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint

/************* SearchFragment ***************
 * 메인화면(검색 탭) (Fragment)
 * 검색 화면
 ********************************************** ***/
@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    companion object {
        const val TAG = "SearchFragment"
    }
    lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchBinding.bind(view)

        val imm =
            parentActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        val recommendList = resources.getStringArray(R.array.fast_search_list)
        recommendList.forEach { name ->
            binding.searchRecommendList.addView(
                OptionItemView(requireContext(), viewLifecycleOwner, name).apply { 
                    setOnClickListener { 
                        // 추천검색어 클릭
                        viewModel.searchExhibition(text.toString()) // 해당 키워드로 검색
                        imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0) // 키보드 내리기
                        binding.searchRecommendList.visibility = View.GONE
                        binding.searchRecommendTitle.visibility = View.GONE
                        binding.searchEditText.text = null // 검색어 초기화
                    }
                }
            )
        }


        binding.searchEditText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.action == KeyEvent.ACTION_DOWN) {
                // Done or Enter 눌렀을 시
                viewModel.searchExhibition(textView.text.toString()) // 검색 결과 업데이트
                imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0) // 키보드 내리기
                binding.searchRecommendList.visibility = View.GONE
                binding.searchRecommendTitle.visibility = View.GONE
                binding.searchEditText.text = null // 검색어 초기화
            }
            return@setOnEditorActionListener false
        }

        viewModel.exhibitionList.observe(viewLifecycleOwner) { exhibitionList ->
            if (exhibitionList.isEmpty()) {
                binding.searchNoItem.visibility = View.VISIBLE
            } else {
                binding.searchNoItem.visibility = View.INVISIBLE
            }
            binding.searchRecyclerview.adapter = SearchAdapter(exhibitionList).apply {
                // RecyclerView 업데이트
                itemClickListener = object : SearchAdapter.OnItemClickListener {
                    override fun onItemClick(
                        holder: SearchAdapter.ViewHolder,
                        view: View,
                        position: Int
                    ) {
                        // 아이템 클릭 시
                        val exhibitionInfo = viewModel.getExhibitionInfo(position)
                        moveToExhibitionDetail(exhibitionInfo)
                    }

                    override fun onLikeBtnClick(
                        holder: SearchAdapter.ViewHolder,
                        view: View,
                        position: Int
                    ) {
                        // 하트 클릭 시
                    }
                }
            }
        }
    }

    private fun moveToExhibitionDetail(exhibitionInfo: Exhbt) {
        // ExhibitionDetail Fragment 로 이동
        when (exhibitionInfo.eb_yn) {
            "Y" -> {
                val bundle = Bundle().apply {
                    putParcelable(EarlyBirdDetailViewModel.EARLYBIRD_INFO, exhibitionInfo)
                }
                parentActivity.supportFragmentManager.replaceFragment(
                    EarlyBirdDetailFragment().apply {
                        arguments = bundle
                    },
                    true
                )
            }

            "N" -> {
                val bundle = Bundle().apply {
                    putParcelable(ExhibitionDetailViewModel.EXHIBITION_INFO, exhibitionInfo)
                }
                parentActivity.supportFragmentManager.replaceFragment(
                    ExhibitionDetailFragment().apply {
                        arguments = bundle
                    },
                    true
                )
            }
        }
    }
}