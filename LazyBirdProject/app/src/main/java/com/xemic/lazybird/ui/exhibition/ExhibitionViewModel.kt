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

/************* ExhibitionViewModel ***************
 * 메인화면(전시 탭) (ViewModel)
 * 전시 정보 전체 보기
 * Todo : 옵션 버튼 List string.xml 로 따로 빼기
 ********************************************** ***/
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
        getExhbtList()
        getOptionItemList()
    }

    private fun initToken() = viewModelScope.launch {
        // dataStore 에서 토큰 값 가져오기
        token = repository.getPreferenceFlow().first()
    }

    fun getExhbtList() = viewModelScope.launch {
        // 전시리스트 받기
        repository.getExhibitionList(token).let { response ->
            if (response.body() != null) {
                _exhbtList.postValue(response.body()!!.exhbtList)
            } else {
                Log.e(TAG, "response.body() is null")
            }
        }
    }

    fun getCustomExhbtList() = viewModelScope.launch {
        // 맞춤형 전시리스트 받기
        repository.getCustomExhibitionList(token).let { response ->
            if (response.body() != null) {
                _exhbtList.postValue(response.body()!!.exhbtList)
            } else {
                Log.e(TAG, "response.body() is null")
            }
        }
    }

    fun getFilterExhbtList(searchList: String) = viewModelScope.launch {
        // 필터링한 전시리스트 받기
        repository.filterExhbtList(token, searchList).let { response ->
            if (response.body() != null) {
                _exhbtList.postValue(response.body()!!.exhbtList)
            } else {
                Log.e(TAG, "response.body() is null")
            }
        }
    }

    private fun getOptionItemList() {
        // 빠른 옵션 버튼명 가져오기
        _optionItemList.value = listOf(
            "회화",
            "조형",
            "사진",
            "특별전",
            "아동전시"
        )
    }

    suspend fun clickLike(exhibitionInfo: Exhbt, is_like: Boolean) {
        // 좋아요 버튼 클릭
        if(is_like){
            repository.exhbtLikeDel(token, exhibitionInfo.exhbt_cd) // 좋아요 버튼 취소
        } else {
            repository.exhbtLikeSave(token, exhibitionInfo.exhbt_cd) // 좋아요 버튼 누르기
        }
    }

    // 전시 리스트 정보 가져오기
    fun getExhibitionInfo(idx: Int): Exhbt = exhbtList.value?.get(idx)!!
}