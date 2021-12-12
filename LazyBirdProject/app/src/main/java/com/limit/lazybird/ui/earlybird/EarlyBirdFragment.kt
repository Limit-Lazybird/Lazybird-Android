package com.limit.lazybird.ui.earlybird

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentEarlybirdBinding
import com.limit.lazybird.models.retrofit.Exhbt
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.ui.earilybirdDetail.EarlyBirdDetailViewModel
import com.limit.lazybird.ui.earlybirdDetail.EarlyBirdDetailFragment
import com.limit.lazybird.ui.earlycard.EarlycardFragment
import com.limit.lazybird.ui.notification.NotificationFragment
import com.limit.lazybird.util.replaceFragment
import com.limit.lazybird.util.toDp
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

/************* EarlyBirdFragment ***************
 * 메인화면(얼리버드 탭) (Fragment)
 * 얼리버드 정보 리스트로 보기
 ********************************************** ***/
@AndroidEntryPoint
class EarlyBirdFragment : Fragment(R.layout.fragment_earlybird) {

    companion object {
        const val TAG = "EarlyBirdFragment"
    }

    private val SCREEN_PAGE_RATIO = 0.90f // ViewPager2에서 현재 페이지의 가로 크기 값 (기기의 몇퍼센트인지)
    private val OFF_SCREEN_PAGE_LIMIT = 1 // ViewPager2에서 보여줄 다음 페이지 개수
    private val OFF_SCREEN_PAGE_RATIO = 0.88f // ViewPager2에서 다음 페이지의 크기감소 비율

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
                    addTransformer { pageView: View, position: Float ->
                        var v = 1 - abs(position)
                        if (position < 0) {
                            // 왼쪽에 있는 페이지
                            pageView.transitionAlpha = 1f // 투명도 100%로 고정
                        } else {
                            // 오른쪽에 있는 페이지
                            pageView.x = -50 * v + 50f.toDp(view) // N+i 번째는 오른쪽으로 (50 x i) 만큼 이동
                            pageView.elevation = v // i 가 작을 수록 윗 장으로 올라오도록 하기
                            pageView.scaleX = SCREEN_PAGE_RATIO
                            pageView.scaleY =
                                (OFF_SCREEN_PAGE_RATIO + (1 - OFF_SCREEN_PAGE_RATIO) * v) // 뒷 장을 갈수록 크기의 비율
                            pageView.transitionAlpha =
                                OFF_SCREEN_PAGE_LIMIT + v // 제일 끝 장 없어질 때 fade out 으로 없어지기
                            binding.earlybirdViewpager2.offscreenPageLimit =
                                OFF_SCREEN_PAGE_LIMIT // LIMIT 개수 제외 나머지 안보이게 하기
                        }
                    }
                }
            )
        }
        binding.earlybirdBell.setOnClickListener {
            // 알림 버튼 클릭
            moveToNotification()
        }
        binding.earlybirdEarlycard.setOnClickListener {
            // 얼리카드 버튼 클릭
            moveToEarlyCard()
        }
    }

    private fun moveToEarlyBirdDetail(earlyBirdInfo: Exhbt) {
        // EarlyBirdDetail Fragment 로 이동
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

    private fun moveToNotification() {
        // Notification Fragment 로 이동
        parentActivity.supportFragmentManager.replaceFragment(NotificationFragment())
    }

    private fun moveToEarlyCard() {
        // Earlycard Fragment 로 이동
        parentActivity.supportFragmentManager.replaceFragment(EarlycardFragment())
    }
}