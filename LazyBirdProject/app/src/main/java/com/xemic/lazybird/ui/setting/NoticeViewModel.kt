package com.xemic.lazybird.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xemic.lazybird.models.NoticeInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(

):ViewModel() {

    private val _noticeList = MutableLiveData<List<NoticeInfo>>()
    val noticeList: LiveData<List<NoticeInfo>> get() = _noticeList

    init {
        initNoticeListDummy()
    }

    private fun initNoticeListDummy() {
        _noticeList.postValue(
            listOf(
                NoticeInfo(
                    "2021년 11월 23일",
                    "레이지버드 1.0.1 버전 출시",
                    "안녕하세요 레이지버드에서 알립니다.\n" +
                            "레이지버드 어플의 1.0.1 버전이 앱스토어와 플레이스토어에 정식 출시되었습니다.\n" +
                            "편리한 어떤 기능과 그런 기능을 추가했습니다.\n" +
                            "지금 바로 다운받아보실 수 있습니다.\n" +
                            "꼭 다운받아보세요.\n" +
                            "잘부탁드립니다.\n" +
                            "언제나 화이팅입니다\n" +
                            "아자아자 화이팅 ~"
                ),
                NoticeInfo(
                    "2021년 11월 21일",
                    "공지사항이니까 꼭 보세요",
                    "보셨군요 아주 좋아요 ^_________^"
                )
            )
        )
    }
}