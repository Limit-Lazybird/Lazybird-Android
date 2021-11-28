package com.xemic.lazybird.ui.earlybird

import android.util.Log
import androidx.lifecycle.*
import com.xemic.lazybird.models.EarlyBirdInfo
import com.xemic.lazybird.models.retrofit.Exhbt
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
    private val repository: EarlyBirdRepository
) : ViewModel() {

    companion object {
        private val TAG = "EarlyBirdViewModel"
    }

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


    val preferenceFlow = repository.getPreferenceFlow()

    init {
        viewModelScope.launch {
            repository.getPreferenceFlow().first().also { token ->
                getEarlyList(token)
            }
        }
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