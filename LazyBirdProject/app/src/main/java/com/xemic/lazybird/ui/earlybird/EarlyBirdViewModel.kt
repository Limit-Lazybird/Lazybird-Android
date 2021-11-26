package com.xemic.lazybird.ui.earlybird

import android.util.Log
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.*
import com.xemic.lazybird.data.PreferenceDataStoreManager
import com.xemic.lazybird.models.EarlyBirdInfo
import com.xemic.lazybird.models.Todo
import com.xemic.lazybird.models.retrofit.Exhbt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EarlyBirdViewModel @Inject constructor(
    private val repository: EarlyBirdRepository
) : ViewModel() {

    private val TAG = "EarlyBirdViewModel"

//    private val _todos = MutableLiveData<List<Todo>>()
//    val todos: LiveData<List<Todo>> = _todos

    private val _earlyList = MutableLiveData<List<Exhbt>>()
    private val earlyList: LiveData<List<Exhbt>> get() = _earlyList

    private val _todayEarlyBirdList = MutableLiveData<List<EarlyBirdInfo>>()
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
//        getTodos()
//        getTodayEarlyBird()
        viewModelScope.launch {
            repository.getPreferenceFlow().first().also { token ->
                getEarlyList(token)
            }
        }
    }

    fun getEarlyInfo(idx: Int): Exhbt = earlyList.value?.get(idx)!!

    private fun getEarlyList(token: String) = viewModelScope.launch {
        repository.getEarlyList(token).let { response ->
            if (response.body() != null) {
                _earlyList.postValue(response.body()!!.exhbtList)
            } else {
                Log.e(TAG, "response.body() is null")
            }
        }
    }

    private fun getTodayEarlyBird() {
//        repository.getTodayEarlyBird().let { response ->
//            _todayEarlyBirdList.postValue(response.body())
//        }
        _todayEarlyBirdList.value = listOf(
            EarlyBirdInfo(
                "미구엘 슈발리에 제주 특별전",
                "https://img1.daumcdn.net/thumb/R1280x0.fjpg/?fname=http://t1.daumcdn.net/brunch/service/user/XoC/image/w0gDroK5Q9vJacqEG4ZUm-N0nrk.jpg",
                42
            ),
            EarlyBirdInfo(
                "살바도르 달리전",
                "https://mblogthumb-phinf.pstatic.net/20141117_247/ahn3607_1416215822249ADUQI_PNG/Screen_Shot_2014-08-28_at_9.02.48_PM.png?type=w2",
                50
            ),
            EarlyBirdInfo(
                "데이비드 슈리글리전",
                "https://img.insight.co.kr/static/2019/07/14/700/lqt17fqx2f2040y32gf0.jpg",
                30
            )
        )
    }

//    private fun getTodos() = viewModelScope.launch {
//        repository.getTodos().let { response ->
//            _todos.postValue(response.body())
//        }
//    }
}