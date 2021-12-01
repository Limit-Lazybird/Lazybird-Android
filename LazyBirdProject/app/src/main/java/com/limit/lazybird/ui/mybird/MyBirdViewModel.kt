package com.limit.lazybird.ui.mybird

import android.util.Log
import androidx.lifecycle.*
import com.limit.lazybird.models.ExhibitionInfoShort
import com.limit.lazybird.models.UserInfo
import com.limit.lazybird.models.retrofit.Exhbt
import com.limit.lazybird.util.dateFormatted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/************* MyBirdViewModel ***************
 * 메인화면(마이버드 탭) (ViewModel)
 * 마이버드 화면 (내 정보 보기)
 ********************************************** ***/
@HiltViewModel
class MyBirdViewModel @Inject constructor(
    private val repository: MyBirdRepository
):ViewModel() {

    companion object {
        const val TAG = "MyBirdViewModel"
    }

    private lateinit var token: String
    private val _likeExhbtList = MutableLiveData<List<Exhbt>>()
    val likeExhbtList: LiveData<List<Exhbt>> get() = _likeExhbtList

    private val _reservationExhbtList = MutableLiveData<List<Exhbt>>()
    val reservationExhbtList: LiveData<List<Exhbt>> get() = _reservationExhbtList

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> get() = _userInfo

    val likeExhibitionShortList:LiveData<List<ExhibitionInfoShort>> get() =
        likeExhbtList.map { exhibitionList ->
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
    val reservationExhibitionShortList:LiveData<List<ExhibitionInfoShort>> get() =
        reservationExhbtList.map { exhibitionList ->
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
        viewModelScope.launch {
            initToken()
            initUserInfo()
            getLikeExhbtList()
            getReservationExhbtList()
        }
    }

    private fun initUserInfo() = viewModelScope.launch {
        // UserInfo(사용자 정보) 초기화
        _userInfo.postValue(repository.getPreferenceUserInfoFlow().first())
    }

    private fun initToken() = viewModelScope.launch {
        // Token 정보 초기화
        token = repository.getPreferenceTokenFlow().first()
    }

    private fun getLikeExhbtList() = viewModelScope.launch {
        // 내가 찜한 전시 리스트 받기
        if(repository.getLikeExhibitionList(token).body() != null){
            val likeList = repository.getLikeExhibitionList(token).body()!!.exhbtList
            _likeExhbtList.postValue(likeList)
        } else {
            Log.e(TAG, "getLikeExhbtList(): ${repository.getLikeExhibitionList(token).errorBody().toString()}")
        }
    }

    private fun getReservationExhbtList() = viewModelScope.launch {
        // 내가 예약한 전시 리스트 받기
        if(repository.getReservationExhibitionList(token).body() != null){
            val reservationList = repository.getReservationExhibitionList(token).body()!!.exhbtList
            _reservationExhbtList.postValue(reservationList)
        } else {
            Log.e(TAG, "getReservationExhbtList(): ${repository.getReservationExhibitionList(token).errorBody().toString()}")
        }
    }

    // 찜한 전시 리스트 정보 가져오기
    fun getLikeExhibitionInfo(idx: Int): Exhbt = likeExhbtList.value?.get(idx)!!

    // 예약한 전시 리스트 정보 가져오기
    fun getReservationExhibitionInfo(idx: Int): Exhbt = reservationExhbtList.value?.get(idx)!!
}