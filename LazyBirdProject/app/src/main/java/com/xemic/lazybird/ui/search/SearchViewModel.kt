package com.xemic.lazybird.ui.search

import android.util.Log
import androidx.lifecycle.*
import com.xemic.lazybird.models.ExhibitionInfoShort
import com.xemic.lazybird.models.retrofit.Exhbt
import com.xemic.lazybird.ui.exhibition.ExhibitionViewModel
import com.xemic.lazybird.util.dateFormatted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
):ViewModel() {
    private lateinit var token: String
    private val _exhbtList = MutableLiveData<List<Exhbt>>()
    val exhbtList: LiveData<List<Exhbt>> get() = _exhbtList
    val exhibitionList: LiveData<List<ExhibitionInfoShort>>
        get() =
        exhbtList.map { exhibitionList ->
            exhibitionList.map { exhbt ->
                ExhibitionInfoShort(
                    title = exhbt.exhbt_nm,
                    place = exhbt.exhbt_lct,
                    startDate = exhbt.exhbt_from_dt.dateFormatted(),
                    endDate = exhbt.exhbt_to_dt.dateFormatted(),
                    discount = exhbt.dc_percent?.removeSuffix("%")?.toInt() ?: 0,
                    price = exhbt.exhbt_prc.removeSuffix("원").replace(",", "").toInt(),
                    discountedPrice = exhbt.dc_prc?.removeSuffix("원")?.replace(",", "")?.toInt()
                        ?: exhbt.exhbt_prc.removeSuffix("원").replace(",", "").toInt(), // null 이면 price 값 출력
                    thumbnailImageUrl = exhbt.exhbt_sn,
                    isLiked = false // Todo : 추가하기
                )
            }
        }

    init {
        initToken()
    }

    fun getExhibitionInfo(idx: Int): Exhbt = exhbtList.value?.get(idx)!!

    private fun initToken() = viewModelScope.launch {
        token = repository.getPreferenceFlow().first()
    }

    suspend fun searchExhibition(words:String) = viewModelScope.launch {
        repository.searchExhibitionList(token, words).let { response ->
            if (response.body() != null) {
                _exhbtList.postValue(response.body()!!.exhbtList)
            } else {
                Log.e(ExhibitionViewModel.TAG, "response.body() is null")
            }
        }
    }
}