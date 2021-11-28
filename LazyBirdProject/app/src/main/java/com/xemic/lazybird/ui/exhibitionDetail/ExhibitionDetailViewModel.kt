package com.xemic.lazybird.ui.exhibitionDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xemic.lazybird.api.ApiHelper
import com.xemic.lazybird.data.PreferenceDataStoreManager
import com.xemic.lazybird.models.ExhibitionInfo
import com.xemic.lazybird.models.retrofit.Exhbt
import com.xemic.lazybird.util.calculateDateDiff
import com.xemic.lazybird.util.dateFormatted
import com.xemic.lazybird.util.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/************* ExhibitionDetailViewModel ***************
 * 메인화면(전시 탭) >> 전시 상세보기 (ViewModel)
 * 전시 정보 자세히 보기
 ********************************************** ***/
@HiltViewModel
class ExhibitionDetailViewModel @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
) : ViewModel() {

    companion object {
        private val TAG = "ExhibitionDetailViewModel"
        const val EXHIBITION_INFO = "exhibitionInfo"
    }

    private val _exhibitionInfo = MutableLiveData<ExhibitionInfo>()
    val exhibitionInfo: LiveData<ExhibitionInfo> get() = _exhibitionInfo

    private val _exhibitionLike = MutableLiveData(false)
    val exhibitionLike: LiveData<Boolean> get() = _exhibitionLike

    private lateinit var token: String

    init {
        initToken()
    }

    private fun initToken() = viewModelScope.launch {
        token = dataStoreManager.preferenceTokenFlow.first()
    }

    fun updateExhibitionInfo(exhbt: Exhbt) {
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
            apiHelper.exhbtLikeDel(token, exhibitionInfo.value!!.id) // 좋아요 버튼 취소
        } else {
            apiHelper.exhbtLikeSave(token, exhibitionInfo.value!!.id) // 좋아요 버튼 활성화
        }
        _exhibitionLike.value = !exhibitionLike.value!!
    }
}