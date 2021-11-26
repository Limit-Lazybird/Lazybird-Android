package com.xemic.lazybird.ui.earilybirdDetail

import android.util.Log
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

@HiltViewModel
class EarlyBirdDetailViewModel @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
) : ViewModel() {

    companion object {
        const val EARLYBIRD_INFO = "earlyBirdInfo"
    }

    private val _exhibitionInfo = MutableLiveData<ExhibitionInfo>()
    val exhibitionInfo: LiveData<ExhibitionInfo> = _exhibitionInfo

    private val _exhibitionLike = MutableLiveData(false)
    val exhibitionLike: LiveData<Boolean> = _exhibitionLike

    private lateinit var token: String

    init {
        initToken()
//        updateExhibitionInfo()
//        updateExhibitionLike()
    }

    private fun initToken() = viewModelScope.launch {
        token = dataStoreManager.preferenceTokenFlow.first()
    }

//    private fun updateExhibitionLike() {
//        _exhibitionLike.value = false
//    }

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
        Log.e("test", exhbt.toString())
        _exhibitionLike.postValue(exhbt.like_yn == "Y")
    }

    /*** deprecated ***/
//    private fun updateExhibitionInfo() {
//        _exhibitionInfo.value = ExhibitionInfo(
//            "미구엘 슈발리에 제주 특별전",
//            10,
//            "아쿠아 플라넷 제주",
//            "2021.11.08",
//            "2021.12.31",
//            50,
//            20000,
//            10000,
//            "얼리버드 티켓은 12월 17일까지 판매됩니다.\n - 매주 월요일은 휴관합니다.\n - 36개월 미만은 입장 불가능합니다.\n",
//            "https://ticketimage.interpark.com/Play/image/etc/21/21010892-01.jpg",
//            "http://img1.tmon.kr/cdn4/deals/2021/11/05/9002271818/summary_ad815.jpg"
//        )
//    }

    suspend fun clickLike() {
        if(exhibitionLike.value!!){
            apiHelper.exhbtLikeDel(token, exhibitionInfo.value!!.id) // 좋아요 버튼 취소
        } else {
            // 좋아요 버튼 누름
            apiHelper.exhbtLikeSave(token, exhibitionInfo.value!!.id) // 좋아요 버튼 취소
        }
        _exhibitionLike.value = !exhibitionLike.value!!
    }
}