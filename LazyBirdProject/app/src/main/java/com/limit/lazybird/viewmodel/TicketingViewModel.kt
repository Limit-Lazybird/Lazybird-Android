package com.limit.lazybird.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.datastore.PreferenceDataStoreManager
import com.limit.lazybird.models.EarlycardInfo
import com.limit.lazybird.models.ExhibitionInfo
import com.limit.lazybird.models.retrofit.EarlyCard
import com.limit.lazybird.repository.EarlycardRepository
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
    private val repository: EarlycardRepository,
):ViewModel() {

    companion object {
        const val TAG = "TicketingViewModel"
        const val EXHIBITION_INFO = "exhibition_info"
    }

    private lateinit var token: String

    private val _exhibitionInfo = MutableLiveData<ExhibitionInfo>()
    val exhibitionInfo: LiveData<ExhibitionInfo> get() = _exhibitionInfo

    private var _earlycardList = MutableLiveData<List<EarlyCard>>()
    val earlycardList: LiveData<List<EarlycardInfo>>
        get() = _earlycardList.map { earlyCardList ->
            earlyCardList.map { earlyCard ->
                EarlycardInfo(
                    no = earlyCard.early_num,
                    title = earlyCard.exhbt_nm,
                    visitDate = if (earlyCard.reser_dt != "N") earlyCard.reser_dt else "-",
                    imgUrl = earlyCard.exhbt_sn
                )
            }
        }

    init {
        initToken()
        initEarlyCardList()
    }

    private fun initToken() = viewModelScope.launch {
        // dataStore 에서 토큰 값 가져오기
        token = repository.getPreferenceTokenFlow().first()
    }

    private fun initEarlyCardList() = viewModelScope.launch {
        repository.getEarlyCardList(token).let { response ->
            _earlycardList.postValue(response.earlyCardList)
        }
    }
    fun updateExhibitionInfo(exhibitionInfo: ExhibitionInfo){
        // 전시정보 업데이트
        _exhibitionInfo.postValue(exhibitionInfo)
    }

    fun updateExhibitionReservation() = viewModelScope.launch {
        // 전시 예약정보 서버에 등록
        repository.exhbtReservationSave(token, exhibitionInfo.value!!.id)
    }
}
