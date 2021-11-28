package com.xemic.lazybird.ui.ticketing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xemic.lazybird.api.ApiHelper
import com.xemic.lazybird.data.PreferenceDataStoreManager
import com.xemic.lazybird.models.ExhibitionInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/************* TicketingViewModel ***************
 * ??? >> 전시 상세정보 >> 예약하기 전 확인 (Fragment)
 * 예약하기 이전의 화면
 ********************************************** ***/
@HiltViewModel
class TicketingViewModel @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
):ViewModel() {

    companion object {
        const val TAG = "TicketingViewModel"
        const val EXHIBITION_INFO = "exhibition_info"
    }

    private lateinit var token: String

    private val _exhibitionInfo = MutableLiveData<ExhibitionInfo>()
    val exhibitionInfo: LiveData<ExhibitionInfo> get() = _exhibitionInfo

    init {
        initToken()
    }

    private fun initToken() = viewModelScope.launch {
        // dataStore 에서 토큰 값 가져오기
        token = dataStoreManager.preferenceTokenFlow.first()
    }

    fun updateExhibitionInfo(exhibitionInfo: ExhibitionInfo){
        // 전시정보 업데이트
        _exhibitionInfo.postValue(exhibitionInfo)
    }

    fun updateExhibitionReservation() = viewModelScope.launch {
        // 전시 예약정보 서버에 등록
        apiHelper.exhbtReservationSave(token, exhibitionInfo.value!!.id)
    }
}