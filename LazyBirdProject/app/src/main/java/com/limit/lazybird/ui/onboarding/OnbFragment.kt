package com.limit.lazybird.ui.onboarding

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.limit.lazybird.R
import com.limit.lazybird.custom.OnbSelectBox
import com.limit.lazybird.databinding.FragmentOnbBinding
import com.limit.lazybird.models.Answer
import com.limit.lazybird.models.DialogInfo
import com.limit.lazybird.models.Survey
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/************* OnbFragment ***************
 * 온보딩 시작화면 >> 온보딩 화면 (Fragment)
 * 온보딩 설문조사하는 화면
 ********************************************** ***/
@AndroidEntryPoint
class OnbFragment : Fragment(R.layout.fragment_onb) {

    companion object {
        const val TAG = "OnbFragment"
    }

    private lateinit var binding: FragmentOnbBinding
    private val viewModel: OnbViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentOnbBinding.bind(view)

        viewModel.customizedList.observe(viewLifecycleOwner) { customizedList ->
            var curPage = 0
            val questionList = mutableListOf<String>()
            val answerList = mutableListOf<MutableList<Answer>>()
            customizedList.forEach { customInfo ->
                if(curPage != customInfo.cq_index){
                    // 새로운 페이지
                    curPage++
                    answerList.add(mutableListOf())
                    questionList.add(customInfo.cq_head)
                }
                answerList[curPage-1].add(Answer(
                    customInfo.cs_head,
                    "https://limit-lazybird.com/customized/image/onb${customInfo.cq_index}_opt${customInfo.cs_index}.png"
                ))
            }

            // 성향분석 리스트
            val surveyList = questionList.mapIndexed { idx, question ->
                Survey(question, answerList[idx])
            }
            viewModel.initSelectedResult(surveyList.size) // selectedResult init

            // 성향분석 리스트 정리 완료 시, 화면에 그려주기 시작
            viewModel.page.observe(viewLifecycleOwner) { page ->
                binding.page = page
                if (page >= surveyList.size) {
                    updateResultToServer() // 서버에 결과 업데이트
                    moveToEndFragment() // 화면 이동
                } else if(page < 0) {
                    moveToStartFragment() // 화면 벗어나기
                } else {
                    val survey = surveyList[page]
                    binding.onbQuestion.text = survey.question
                    binding.onbAnswerContainer.removeAllViews() // 일단 LineaerLayout에 속한 View들 모두 제거
                    for (answer in survey.answerList) {
                        // weight 가 1 이 걸려있는 LinearLayout 기반 SelectBoxView 를 정답의 개수만큼 넣어주기
                        binding.onbAnswerContainer.addView(
                            OnbSelectBox(requireContext(), null).apply {
                                setAnswer(answer.answerTitle)
                                setImage(answer.answerImgUrl)
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    1.0f
                                )
                            }
                        )
                    }
                    // 모든 선택지에 onClickListener 달아주기
                    binding.onbAnswerContainer.forEachIndexed { selectedNo, view ->
                        val onbSelectBox = view as OnbSelectBox
                        onbSelectBox.setOnClickListener {
                            // Answer 항목 클릭 시
                            viewModel.clickOptionBox(page, selectedNo)
                            viewModel.moveNextPage()
                        }
                    }
                }
            }
        }

        binding.onbBack.setOnClickListener {
            // 이전 문항 이동 버튼
            viewModel.movePrevPage()
        }

        binding.onbClose.setOnClickListener {
            // 닫기 버튼 클릭 시
            createCancelDialog()
        }

        setFragmentResultListener(CustomDialogFragment.TAG) { _, bundle ->
            when (bundle.getString("resultCode")) {
                CustomDialogFragment.RESULT_OK -> {
                    // CustomDialogFragment 에서 종료 버튼 클릭 시
                    moveToStartFragment()
                }
            }
        }
    }

    private fun createCancelDialog() {
        // 종료 재확인 dialog 생성
        val dialogInfo = DialogInfo(
            title = resources.getString(R.string.onb_cancel_title),
            message = resources.getString(R.string.onb_cancel_message),
            positiveBtnTitle = resources.getString(R.string.onb_cancel_yes),
            negativeBtnTitle = resources.getString(R.string.onb_cancel_no)
        )
       CustomDialogFragment().apply {
            // dialog 정보 보내주기
            arguments = bundleOf().apply {
                putParcelable(CustomDialogFragment.DIALOG_INFO, dialogInfo)
            }
        }.show(
            parentActivity.supportFragmentManager,
            CustomDialogFragment.TAG
        )
    }


    private fun moveToStartFragment() {
        // OnbStartFragment 로 이동
        parentActivity.supportFragmentManager.popBackStack()
    }

    private fun updateResultToServer() {
        // 설문조사 결과 서버에 업데이트
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewModel.deleteCustomizedList()
            viewModel.insertCustomizedList(
                viewModel.selectedResult.map{
                    it+1
                }.joinToString(".")
            )
        }
    }

    private fun moveToEndFragment() {
        // OnbEndFragment로 이동
        parentActivity.supportFragmentManager.replaceFragment(OnbEndFragment(), false)
    }

}