package com.xemic.lazybird.ui.earlybird

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.FragmentEarlybirdBinding
import com.xemic.lazybird.models.EarlyBirdInfo
import com.xemic.lazybird.models.retrofit.Exhbt
import com.xemic.lazybird.ui.MainActivity
import com.xemic.lazybird.ui.earilybirdDetail.EarlyBirdDetailViewModel
import com.xemic.lazybird.ui.earlybirdDetail.EarlyBirdDetailFragment
import com.xemic.lazybird.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlin.math.abs

@AndroidEntryPoint
class EarlyBirdFragment : Fragment(R.layout.fragment_earlybird) {

    companion object {
        const val TAG = "EarlyBirdFragment"
    }

    private val SCREEN_PAGE_RATIO = 0.90f
    private val OFF_SCREEN_PAGE_LIMIT = 1
    private val OFF_SCREEN_PAGE_RATIO = 0.88f

    private lateinit var binding: FragmentEarlybirdBinding
    private val earlyBirdViewModel: EarlyBirdViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEarlybirdBinding.bind(view)

        earlyBirdViewModel.todayEarlyBirdList.observe(viewLifecycleOwner) { earlybirdInfoList ->
            binding.earlybirdViewpager2.adapter = EarlyBirdAdapter(earlybirdInfoList).apply {
                // adapter 적용
                itemClickListener = object : EarlyBirdAdapter.OnItemClickListener {
                    override fun onItemClick(
                        holder: EarlyBirdAdapter.PageViewHolder,
                        view: View,
                        position: Int
                    ) {
                        // item 클릭 시
                        val earlyBirdInfo = earlyBirdViewModel.getEarlyInfo(position)
                        moveToEarlyBirdDetail(earlyBirdInfo)
                    }
                }
            }
            binding.earlybirdViewpager2.apply {
                // 첫 페이지, 마지막페이지 overScroll 효과 차단
                (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                (getChildAt(childCount-1) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            }
            binding.earlybirdViewpager2.setPageTransformer(
                // page 변환 시 애니메이션 적용
                CompositePageTransformer().apply {
                    addTransformer { view: View, position: Float ->
                        var v = 1 - abs(position)
                        if (position < 0) {
                            // 왼쪽에 있는 페이지
                            view.transitionAlpha = 1f // 투명도 100%로 고정
                        } else {
                            // 오른쪽에 있는 페이지
                            view.x = -50 * v // N+i 번째는 오른쪽으로 (50 x i) 만큼 이동
                            view.elevation = v // i 가 작을 수록 윗 장으로 올라오도록 하기, // Todo : 이걸 v+1로 바꾸면 뒤에서 앞으로 오는것도 올라오게 할 수 있을까
                            view.scaleX = SCREEN_PAGE_RATIO
                            view.scaleY =
                                (OFF_SCREEN_PAGE_RATIO + (1 - OFF_SCREEN_PAGE_RATIO) * v) // 뒷 장을 갈수록 크기의 비율
                            view.transitionAlpha =
                                OFF_SCREEN_PAGE_LIMIT + v // 제일 끝 장 없어질 때 fade out 으로 없어지기
                            binding.earlybirdViewpager2.offscreenPageLimit =
                                OFF_SCREEN_PAGE_LIMIT // LIMIT 개수 제외 나머지 안보이게 하기
                        }
                    }
                }
            )
        }

//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            // token 초기화
//            val token = earlyBirdViewModel.preferenceFlow.first()
//            Log.e("test", token)
//        }

//        earlyBirdViewModel.todos.observe(viewLifecycleOwner) { todoList ->
//            for (todo in todoList) {
//                // Log.e("test", todo.toString())
//            }
//        }
//        earlybird_detail_filter_button.setOnClickListener {
//            // Todo : 전시상세검색 올라오도록 기능 구현
//            Toast.makeText(context, "전시상세검색 올라와야하는데.. 아직 개발안함", Toast.LENGTH_SHORT).show()
//        }
//        flexbox_layout.addView(
//            OptionItemView(requireContext(), viewLifecycleOwner).apply {
//                text = "이것도 한번 눌러보시지!"
//            }
//        )
//        flexbox_layout.addView(
//            OptionItemView(requireContext(), viewLifecycleOwner).apply {
//                text = "hello world! by option"
//            }
//        )
//        flexbox_layout.addView(
//            OptionItemView(requireContext(), viewLifecycleOwner).apply {
//                text = "asdfawefasdfawefawefasdfawefasdfawefawef"
//            }
//        )
    }

    private fun moveToEarlyBirdDetail(earlyBirdInfo: Exhbt) {
//        setFragmentResult(TAG, bundleOf("title" to earlyBirdInfo.exhbt_nm))
//        val bundle = Bundle().apply {
//            putParcelable(EarlyBirdDetailViewModel.EARLYBIRD_INFO, earlyBirdInfo)
//        }
//        setFragmentResult(EarlyBirdDetailFragment.TAG, bundle)
        val bundle = Bundle().apply {
            putParcelable(EarlyBirdDetailViewModel.EARLYBIRD_INFO, earlyBirdInfo)
        }
        parentActivity.supportFragmentManager.replaceFragment(
            EarlyBirdDetailFragment().apply {
                arguments = bundle
            },
            true
        )
    }
}