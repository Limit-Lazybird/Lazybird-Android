package com.limit.lazybird.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limit.lazybird.models.ExhibitionInfo
import com.limit.lazybird.models.retrofit.Exhbt
import com.limit.lazybird.repository.ExhibitionRepository
import com.limit.lazybird.util.calculateDateDiff
import com.limit.lazybird.util.dateFormatted
import com.limit.lazybird.util.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/************* EarlyBirdDetailViewModel ***************
 * 메인화면(얼리버드 탭) >> 얼리버드 상세보기 (ViewModel)
 * 얼리버드 정보 자세히 보기
 ********************************************** ***/
@HiltViewModel
class EarlyBirdDetailViewModel @Inject constructor(
    private val repository: ExhibitionRepository
) : ViewModel() {

    companion object {
        const val TAG = "EarlyBirdDetailViewModel"
        const val EARLYBIRD_INFO = "earlyBirdInfo"
    }

    private lateinit var token: String

    private val _exhibitionInfo = MutableLiveData<ExhibitionInfo>()
    val exhibitionInfo: LiveData<ExhibitionInfo> = _exhibitionInfo

    private val _exhibitionLike = MutableLiveData(false)
    val exhibitionLike: LiveData<Boolean> = _exhibitionLike

    init {
        initToken()
    }

    private fun initToken() = viewModelScope.launch {
        // dataStore 에서 토큰 값 가져오기
        token = repository.getPreferenceTokenFlow().first()
    }

    fun updateExhibitionInfo(exhbt: Exhbt) {
        // Exhbt 정보 업데이트
        _exhibitionInfo.postValue(
            ExhibitionInfo(
                id = exhbt.exhbt_cd,
                title = exhbt.exhbt_nm,
                dDay = calculateDateDiff(exhbt.exhbt_to_dt.toDate(), Calendar.getInstance().time),
                place = exhbt.exhbt_lct,
                startDate = exhbt.exhbt_from_dt.dateFormatted(),
                endDate = exhbt.exhbt_to_dt.dateFormatted(),
                discount = exhbt.dc_percent?.removeSuffix("%")?.toInt() ?: 0,
                price = exhbt.exhbt_prc.removeSuffix("원").replace(",", "").toInt(),
                discountedPrice = exhbt.dc_prc?.removeSuffix("원")?.replace(",", "")?.toInt()
                    ?: exhbt.exhbt_prc.removeSuffix("원").replace(",", "").toInt(), // null 이면 price 값 출력
                notice = exhbt.exhbt_notice ?: "",
                thumbnailImageUrl = exhbt.exhbt_sn,
                exhibitionDetailImgUrl = exhbt.dt_img,
                exhibitionUrl = exhbt.excbt_url,
                like_yn = exhbt.like_yn == "Y"
            )
        )

        _exhibitionLike.postValue(exhbt.like_yn == "Y")
    }

    fun clickLike() = viewModelScope.launch {
        // 좋아요 버튼 클릭
        if(exhibitionLike.value!!){
            repository.exhbtLikeDel(token, exhibitionInfo.value!!.id) // 좋아요 버튼 취소
        } else {
            repository.exhbtLikeSave(token, exhibitionInfo.value!!.id) // 좋아요 버튼 활성화
        }
        _exhibitionLike.value = !exhibitionLike.value!!
    }
}