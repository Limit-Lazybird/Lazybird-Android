package com.xemic.lazybird.ui.search

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
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
import kotlinx.coroutines.launch

/************* SearchFragment ***************
 * 메인화면(검색 탭) (Fragment)
 * 검색 화면
 * Todo : 추천검색어 클릭 시 그 단어로 검색되도록 기능 구현
 * Todo : 추천검색리스트 string.xml 로 따로 빼기
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

        val recommendList = listOf(
            "회화",
            "조형",
            "사진",
            "특별전",
            "아동전시"
        )

        recommendList.forEach { name ->
            binding.searchRecommendList.addView(
                OptionItemView(requireContext(), viewLifecycleOwner, name)
            )
        }

        binding.searchEditText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.action == KeyEvent.ACTION_DOWN) {
                // Done or Enter 눌렀을 시
                viewLifecycleOwner.lifecycle.coroutineScope.launch {
                    imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0) // 키보드 내리기
                    viewModel.searchExhibition(textView.text.toString()) // 검색 결과 업데이트
                    binding.searchRecommendList.visibility = View.GONE
                    binding.searchRecommendTitle.visibility = View.GONE
                    binding.searchEditText.setText("") // 검색어 초기화
                }
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