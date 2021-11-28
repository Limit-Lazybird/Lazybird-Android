package com.xemic.lazybird.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xemic.lazybird.models.retrofit.NotificationInfo
import com.xemic.lazybird.ui.mybird.MyBirdRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/************** NotificationViewModel ****************
 * ?? >> 알림화면 (ViewModel)
 * 나에게 온 알림 보기
 ********************************************** ***/
@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository
): ViewModel() {

    companion object {
        const val TAG = "NotificationViewModel"
    }

    private lateinit var token: String
    private val _notificationList = MutableLiveData<List<NotificationInfo>>()
    val notificationList:LiveData<List<NotificationInfo>> get() = _notificationList

    init {
        initToken()
        initNotificationList()
    }

    private fun initToken() = viewModelScope.launch {
        // Token 정보 초기화
        token = repository.getPreferenceTokenFlow().first()
    }

    private fun initNotificationList() {
        _notificationList.postValue(
            listOf(
                NotificationInfo(
                    "전시 일정",
                    "예매하신 어떤 전시가 이정도 남았어요",
                    "12시간 전"
                ),
                NotificationInfo(
                    "얼리버드",
                    "새로운 얼리버드 정보가 올라왔어요",
                    "12시간 전"
                ),
                NotificationInfo(
                    "전시 일정",
                    "알림에는 무슨 내용을 넣으면 좋을까",
                    "11월 24일"
                ),
                NotificationInfo(
                    "전시 일정",
                    "알림에는 무슨 내용을 넣으면 좋을까",
                    "11월 24일"
                ),
                NotificationInfo(
                    "전시 일정",
                    "알림에는 무슨 내용을 넣으면 좋을까",
                    "11월 24일"
                ),
            )
        )
    }

}