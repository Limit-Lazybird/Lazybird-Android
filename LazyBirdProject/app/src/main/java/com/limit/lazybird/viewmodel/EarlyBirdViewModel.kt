package com.limit.lazybird.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.limit.lazybird.models.EarlyBirdInfo
import com.limit.lazybird.models.retrofit.Exhbt
import com.limit.lazybird.repository.ExhibitionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/************* EarlyBirdViewModel ***************
 * 메인화면(얼리버드 탭) (ViewModel)
 * 얼리버드 정보 리스트로 보기
 ********************************************** ***/
@HiltViewModel
class EarlyBirdViewModel @Inject constructor(
    private val repository: ExhibitionRepository
) : ViewModel() {

    companion object {
        private val TAG = "EarlyBirdViewModel"
    }

    private lateinit var token: String

    private val _earlyList = MutableLiveData<List<Exhbt>>()
    private val earlyList: LiveData<List<Exhbt>> get() = _earlyList

    val todayEarlyBirdList: LiveData<List<EarlyBirdInfo>>
        get() =
            earlyList.map { exhibitionList ->
                exhibitionList.map { exhbt ->
                    EarlyBirdInfo(
                        title = exhbt.exhbt_nm,
                        imageUrl = exhbt.exhbt_sn,
                        discount = exhbt.dc_percent?.removeSuffix("%")?.toInt() ?: 0
                    )
                }
            }

    init {
        initToken()
        getEarlyList(token)
    }

    private fun initToken() = viewModelScope.launch {
        // Token 정보 초기화
        token = repository.getPreferenceTokenFlow().first()
    }

    private fun getEarlyList(token: String) = viewModelScope.launch {
        // 얼리버드 전시리스트 받기
        repository.getEarlyList(token).let { response ->
            if (response.body() != null) {
                _earlyList.postValue(response.body()!!.exhbtList)
            } else {
                Log.e(TAG, "response.body() is null")
            }
        }
    }

    // 얼리버드 전시 리스트 정보 가져오기
    fun getEarlyInfo(idx: Int): Exhbt = earlyList.value?.get(idx)!!
}