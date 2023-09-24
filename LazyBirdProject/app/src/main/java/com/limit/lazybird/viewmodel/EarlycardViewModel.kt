package com.limit.lazybird.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.limit.lazybird.models.EarlycardInfo
import com.limit.lazybird.models.retrofit.EarlyCard
import com.limit.lazybird.repository.EarlycardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/************* EarlyCardFragment ***************
 * 메인화면(?? 탭) >> 얼리카드 화면 (ViewModel)
 * 얼리카드 정보 리스트로 보기
 ********************************************** ***/
@HiltViewModel
class EarlycardViewModel @Inject constructor(
    private val repository: EarlycardRepository
) : ViewModel() {

    companion object {
        private val TAG = "EarlycardViewModel"
    }

    private lateinit var token: String

    private var _earlycardList = MutableLiveData<List<EarlyCard>>()
    val earlycardList: LiveData<List<EarlycardInfo>>
        get() = _earlycardList.map { earlyCardList ->
            earlyCardList.map { earlyCard ->
                EarlycardInfo(
                    no = earlyCard.early_num,
                    title = earlyCard.exhbt_nm,
                    visitDate = if (earlyCard.reser_dt != "N") earlyCard.reser_dt.substring(0, 4)+"-"+earlyCard.reser_dt.substring(4, 6)+"-"+earlyCard.reser_dt.substring(6, 8) else "-",
                    imgUrl = earlyCard.exhbt_sn
                )
            }
        }

    init {
        initToken()
        initEarlyCardList()
    }

    private fun initEarlyCardList() = viewModelScope.launch {
        repository.getEarlyCardList(token).let { response ->
            _earlycardList.postValue(response.earlyCardList)
        }
    }

    private fun initToken() = viewModelScope.launch {
        // Token 정보 초기화
        token = repository.getPreferenceTokenFlow().first()
    }
}
