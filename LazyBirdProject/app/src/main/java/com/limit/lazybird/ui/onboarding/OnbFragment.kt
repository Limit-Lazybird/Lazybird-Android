package com.limit.lazybird.ui.onboarding

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.limit.lazybird.R
import com.limit.lazybird.ui.custom.OnbSelectBox
import com.limit.lazybird.databinding.FragmentOnbBinding
import com.limit.lazybird.models.Answer
import com.limit.lazybird.models.DialogInfo
import com.limit.lazybird.models.DialogResult
import com.limit.lazybird.models.Survey
import com.limit.lazybird.ui.BaseFragment
import com.limit.lazybird.ui.custom.dialog.CustomDialogFragment
import com.limit.lazybird.viewmodel.OnbViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/************* OnbFragment ***************
 * 온보딩 시작화면 >> 온보딩 화면 (Fragment)
 * 온보딩 설문조사하는 화면
 ********************************************** ***/
@AndroidEntryPoint
class OnbFragment : BaseFragment<FragmentOnbBinding>(FragmentOnbBinding::inflate) {

    private val viewModel: OnbViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this

        viewModel.customizedList.observe(viewLifecycleOwner) { customizedList ->
            var curPage = 0
            val questionList = mutableListOf<String>()
            val answerList = mutableListOf<MutableList<Answer>>()
            customizedList.forEach { customInfo ->
                if (curPage != customInfo.cq_index) {
                    // 새로운 페이지
                    curPage++
                    answerList.add(mutableListOf())
                    questionList.add(customInfo.cq_head)
                }
                answerList[curPage - 1].add(
                    Answer(
                        customInfo.cs_head,
                        "https://limit-lazybird.com/customized/image/onb${customInfo.cq_index}_opt${customInfo.cs_index}.png"
                    )
                )
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
                } else if (page < 0) {
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

        // 온보딩 중도 종료 Dialog 선택 결과
        navController.currentBackStackEntry?.savedStateHandle?.apply {
            getLiveData<DialogResult>(CustomDialogFragment.TAG)?.observe(viewLifecycleOwner) { dialogResult ->
                when (dialogResult.results[0]) {
                    CustomDialogFragment.RESULT_OK -> {
                        moveToStartFragment()
                    }
                }
            }
        }
    }

    // 이전 문항 이동 버튼
    fun clickBackBtn() {
        viewModel.movePrevPage()
    }

    // 종료 재확인 dialog 생성
    fun createCancelDialog() {
        val dialogInfo = DialogInfo(
            title = resources.getString(R.string.onb_cancel_title),
            message = resources.getString(R.string.onb_cancel_message),
            positiveBtnTitle = resources.getString(R.string.onb_cancel_yes),
            negativeBtnTitle = resources.getString(R.string.onb_cancel_no)
        )
        val action = OnbFragmentDirections.actionOnbFragmentToCustomDialogFragment(dialogInfo)
        navController.navigate(action)
    }


    // OnbStartFragment 로 이동
    private fun moveToStartFragment() {
        navController.popBackStack()
    }

    // 설문조사 결과 서버에 업데이트
    private fun updateResultToServer() {
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewModel.deleteCustomizedList()
            viewModel.insertCustomizedList(
                viewModel.selectedResult.map {
                    it + 1
                }.joinToString(".")
            )
        }
    }

    // OnbEndFragment로 이동
    private fun moveToEndFragment() {
        navController.navigate(OnbFragmentDirections.actionOnbFragmentToOnbEndFragment())
    }
}