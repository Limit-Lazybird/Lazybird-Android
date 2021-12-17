package com.limit.lazybird.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limit.lazybird.models.retrofit.NotificationInfo
import com.limit.lazybird.repository.NotificationRepository
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
    private val _notificationList = MutableLiveData<ArrayList<NotificationInfo>>()
    val notificationList:LiveData<ArrayList<NotificationInfo>> get() = _notificationList

    init {
        initToken()
        initNotificationList()
    }

    private fun initToken() = viewModelScope.launch {
        // Token 정보 초기화
        token = repository.getPreferenceTokenFlow().first()
    }

    fun deleteNotification(position:Int) = viewModelScope.launch {
        _notificationList.postValue(
            _notificationList.value?.apply {
                removeAt(position)
            }
        )
    }

    private fun initNotificationList() {
        // Todo : dummy data
        _notificationList.postValue(
            arrayListOf(
                NotificationInfo(
                    1,
                    "전시 일정",
                    "예매하신 어떤 전시가 이정도 남았어요",
                    "12시간 전"
                ),
                NotificationInfo(
                    2,
                    "얼리버드",
                    "새로운 얼리버드 정보가 올라왔어요",
                    "12시간 전"
                ),
                NotificationInfo(
                    3,
                    "전시 일정",
                    "알림에는 무슨 내용을 넣으면 좋을까",
                    "11월 24일"
                ),
                NotificationInfo(
                    4,
                    "전시 일정",
                    "알림에는 무슨 내용을 넣으면 좋을까",
                    "11월 24일"
                ),
                NotificationInfo(
                    5,
                    "전시 일정",
                    "알림에는 무슨 내용을 넣으면 좋을까",
                    "11월 24일"
                ),
            )
        )
    }

}