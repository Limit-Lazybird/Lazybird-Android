package com.limit.lazybird.ui.setting

import android.util.Log
import androidx.lifecycle.*
import com.limit.lazybird.models.NoticeInfo
import com.limit.lazybird.models.retrofit.Notice
import com.limit.lazybird.ui.exhibition.ExhibitionViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/***************** NoticeViewModel *******************
 * 메인화면(마이버드 탭) >> 옵션 >> 공지사항 (ViewModel)
 * 공지사항 전체 보기
 ********************************************** ***/
@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val repository: NoticeRepository
):ViewModel() {

    companion object {
        const val TAG = "NoticeViewModel"
    }

    private val _noticeList = MutableLiveData<List<Notice>>()
    val noticeInfoList: LiveData<List<NoticeInfo>> get() = _noticeList.map { noticeList ->
        noticeList.map { notice ->
            NoticeInfo(
                date = notice.NOTICE_START_DATE,
                title = notice.NOTICE_TITLE,
                context = notice.NOTICE_CONTENT
            )
        }
    }

    init {
        initNoticeList()
    }

    private fun initNoticeList() = viewModelScope.launch {
        repository.getNoticeList().let { response ->
            if (response.body() != null) {
                _noticeList.postValue(response.body()!!.noticeList)
            } else {
                Log.e(ExhibitionViewModel.TAG, "response.body() is null")
            }
        }
    }
}