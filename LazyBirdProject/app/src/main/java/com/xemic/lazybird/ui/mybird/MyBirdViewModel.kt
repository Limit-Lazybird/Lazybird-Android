package com.xemic.lazybird.ui.mybird

import android.util.Log
import androidx.lifecycle.*
import com.xemic.lazybird.models.ExhibitionInfoShort
import com.xemic.lazybird.models.UserInfo
import com.xemic.lazybird.models.retrofit.Exhbt
import com.xemic.lazybird.util.dateFormatted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        _userInfo.postValue(repository.getPreferenceUserInfoFlow().first())
    }

    private fun initToken() = viewModelScope.launch {
        token = repository.getPreferenceTokenFlow().first()
    }

    private fun getLikeExhbtList() =viewModelScope.launch {
        if(repository.getLikeExhibitionList(token).body() != null){
            val likeList = repository.getLikeExhibitionList(token).body()!!.exhbtList
            _likeExhbtList.postValue(likeList)
        } else {
            Log.e(TAG, "getLikeExhbtList(): ${repository.getLikeExhibitionList(token).errorBody().toString()}")
        }
    }

    private fun getReservationExhbtList() = viewModelScope.launch {
        if(repository.getReservationExhibitionList(token).body() != null){
            val reservationList = repository.getReservationExhibitionList(token).body()!!.exhbtList
            _reservationExhbtList.postValue(reservationList)
        } else {
            Log.e(TAG, "getReservationExhbtList(): ${repository.getReservationExhibitionList(token).errorBody().toString()}")
        }
    }

    fun getLikeExhibitionInfo(idx: Int): Exhbt = likeExhbtList.value?.get(idx)!!

    fun getReservationExhibitionInfo(idx: Int): Exhbt = reservationExhbtList.value?.get(idx)!!
}