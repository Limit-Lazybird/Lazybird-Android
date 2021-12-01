package com.limit.lazybird.ui.search

import android.util.Log
import androidx.lifecycle.*
import com.limit.lazybird.models.ExhibitionInfoShort
import com.limit.lazybird.models.retrofit.Exhbt
import com.limit.lazybird.ui.exhibition.ExhibitionViewModel
import com.limit.lazybird.util.dateFormatted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/************* SearchViewModel ***************
 * 메인화면(검색 탭) (ViewModel)
 * 검색 화면
 ********************************************** ***/
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
):ViewModel() {

    companion object {
        const val TAG = "ExhibitionViewModel"
    }

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
                    isLiked = exhbt.like_yn == "Y"
                )
            }
        }

    init {
        initToken()
    }

    private fun initToken() = viewModelScope.launch {
        // dataStore 에서 토큰 값 가져오기
        token = repository.getPreferenceFlow().first()
    }

    fun searchExhibition(words:String) = viewModelScope.launch {
        // 검색결과 전시리스트 받기
        repository.searchExhibitionList(token, words.trim()).let { response ->
            if (response.body() != null) {
                _exhbtList.postValue(response.body()!!.exhbtList)
            } else {
                Log.e(ExhibitionViewModel.TAG, "response.body() is null")
            }
        }
    }

    // 전시 리스트 정보 가져오기
    fun getExhibitionInfo(idx: Int): Exhbt = exhbtList.value?.get(idx)!!
}