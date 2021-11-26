package com.xemic.lazybird.ui.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xemic.lazybird.models.Answer
import com.xemic.lazybird.models.Survey
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*** deprecated ***/

@HiltViewModel
class OnbStartViewModel @Inject constructor(
    private val repository: OnbStartRepository
) : ViewModel() {
    lateinit var onbSurveyList: List<Survey>

    init {
        getSurveyList()
    }

    private fun getSurveyList() {
//         repository.getSurveyList().let { response ->
//             onbSurveyList.postValue(response.body())
//         }

        // Todo : dummyData
        onbSurveyList = listOf(
            Survey(
                "전시회를 방문하는 목적은?",
                listOf(
                    Answer(
                        "데이트, 친구와의 추억을 위해 가는 편",
                        "https://blog.kakaocdn.net/dn/bazLZW/btqKMN2LHjc/8VaRgqsCKEJ67kcL63WO3k/img.jpg"
                    ),
                    Answer(
                        "예술작품을 즐기기 위해 가는 편",
                        "https://i.ytimg.com/vi/_Ba4h-yl5-0/maxresdefault.jpg"
                    )
                )
            ),
            Survey(
                "전시회에 방문해서 경험하고 싶은 것은?",
                listOf(
                    Answer("특정 분야(회화 사진 등)에 전문적인 작품을 집중해 관람하고 싶다."),
                    Answer("전시회의 전체적인 분위기를 느끼며 관람하고 싶다.")
                )
            ),
            Survey(
                "선호하는 전시회의 분위기는?",
                listOf(
                    Answer("어둡고 몽환적인 분위기"),
                    Answer("신나는 분위기"),
                    Answer("조용하고 모던한 분위기")
                )
            )

        )
    }
}