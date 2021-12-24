package com.limit.lazybird.ui.search

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.limit.lazybird.R
import com.limit.lazybird.ui.custom.OptionItemView
import com.limit.lazybird.databinding.FragmentSearchBinding
import com.limit.lazybird.models.retrofit.Exhbt
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.ui.MainFragmentDirections
import com.limit.lazybird.viewmodel.EarlyBirdDetailViewModel
import com.limit.lazybird.ui.earlybird.EarlyBirdDetailFragment
import com.limit.lazybird.ui.exhibition.ExhibitionDetailFragment
import com.limit.lazybird.viewmodel.ExhibitionDetailViewModel
import com.limit.lazybird.util.replaceFragment
import com.limit.lazybird.viewmodel.SearchViewModel
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

    private lateinit var navController: NavController
    lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = requireView().findNavController()
        binding = FragmentSearchBinding.bind(view)

        val imm =
            parentActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        // 추천검색 리스트 업데이트
        val recommendList = resources.getStringArray(R.array.fast_search_list)
        recommendList.forEach { name ->
            binding.searchRecommendList.addView(
                OptionItemView(requireContext(), viewLifecycleOwner, name).apply { 
                    setOnClickListener { 
                        // 추천검색어 클릭
                        viewModel.searchExhibition(text.toString()) // 해당 키워드로 검색
                        imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0) // 키보드 내리기
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

        // 검색 결과 리스트 업데이트
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
                        when (exhibitionInfo.eb_yn) {
                            "Y" -> {
                                // 얼리버드 전시 디테일 화면
                                moveToEarlyBirdDetail(exhibitionInfo)
                            }

                            "N" -> {
                                // 일반 전시 디테일 화면
                                moveToExhibitionDetail(exhibitionInfo)
                            }
                        }
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

    // ExhibitionDetail Fragment 로 이동
    private fun moveToExhibitionDetail(exhibitionInfo: Exhbt) {
        navController.navigate(MainFragmentDirections.actionMainFragmentToExhibitionDetailFragment(exhibitionInfo))
    }

    // EarlyBirdDetail Fragment 로 이동
    private fun moveToEarlyBirdDetail(exhibitionInfo: Exhbt) {
        navController.navigate(MainFragmentDirections.actionMainFragmentToEarlyBirdDetailFragment(exhibitionInfo))
    }

}