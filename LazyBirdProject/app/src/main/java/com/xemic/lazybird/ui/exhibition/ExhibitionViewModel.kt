package com.xemic.lazybird.ui.exhibition

import android.util.Log
import androidx.lifecycle.*
import com.xemic.lazybird.models.ExhibitionInfoShort
import com.xemic.lazybird.models.retrofit.Exhbt
import com.xemic.lazybird.util.dateFormatted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExhibitionViewModel @Inject constructor(
    private val repository: ExhibitionRepository
): ViewModel() {

    companion object {
        const val TAG = "ExhibitionViewModel"
    }

    lateinit var token: String

    private val _exhbtList = MutableLiveData<List<Exhbt>>()
    val exhbtList: LiveData<List<Exhbt>> get() = _exhbtList

    val exhibitionList:LiveData<List<ExhibitionInfoShort>> get() =
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

    private val _optionItemList = MutableLiveData<List<String>>()
    val optionItemList:LiveData<List<String>> get() = _optionItemList

    init {
        initToken()
        viewModelScope.launch {
            initToken()
            getExhbtList()
        }
        getOptionItemList()
    }

    private fun initToken() = viewModelScope.launch {
        token = repository.getPreferenceFlow().first()
    }

    fun getExhbtList() = viewModelScope.launch {
        repository.getExhibitionList(token).let { response ->
            if (response.body() != null) {
                _exhbtList.postValue(response.body()!!.exhbtList)
            } else {
                Log.e(TAG, "response.body() is null")
            }
        }
    }

    fun getCustomExhbtList() = viewModelScope.launch {
        repository.getCustomExhibitionList(token).let { response ->
            if (response.body() != null) {
                _exhbtList.postValue(response.body()!!.exhbtList)
            } else {
                Log.e(TAG, "response.body() is null")
            }
        }
    }

    fun getFilterExhbtList(searchList: String) = viewModelScope.launch {
        repository.filterExhbtList(token, searchList).let { response ->
            if (response.body() != null) {
                _exhbtList.postValue(response.body()!!.exhbtList)
            } else {
                Log.e(TAG, "response.body() is null")
            }
        }
    }

    fun getExhibitionInfo(idx: Int): Exhbt = exhbtList.value?.get(idx)!!

    private fun getOptionItemList() {
        _optionItemList.value = listOf(
            "회화",
            "조형",
            "사진",
            "특별전",
            "아동전시"
        )
    }

    suspend fun clickLike(exhibitionInfo: Exhbt, is_like: Boolean) {
        if(is_like){
            repository.exhbtLikeDel(token, exhibitionInfo.exhbt_cd) // 좋아요 버튼 취소
        } else {
            repository.exhbtLikeSave(token, exhibitionInfo.exhbt_cd) // 좋아요 버튼 누르기
        }
    }
}