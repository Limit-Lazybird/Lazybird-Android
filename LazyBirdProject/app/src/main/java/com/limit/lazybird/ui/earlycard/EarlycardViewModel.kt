package com.limit.lazybird.ui.earlycard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limit.lazybird.models.EarlycardInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EarlycardViewModel @Inject constructor(
    private val repository: EarlycardRepository
) :ViewModel() {

    companion object {
        private val TAG = "EarlycardViewModel"
    }

    private lateinit var token: String
    private var _earlycardList = MutableLiveData<List<EarlycardInfo>>()
    val earlycardList: LiveData<List<EarlycardInfo>> get() = _earlycardList
    
    init {
        initToken()
        initDummyData()
    }

    private fun initDummyData() {
        _earlycardList.postValue(
            listOf(
                EarlycardInfo(
                    no = 1,
                    title = "영국 테이트 미술관 특별전",
                    visitDate = "2021.12.09",
                    imgUrl = "https://ww.namu.la/s/bd52223e4d1f11fcc4c7f6506bf3321b26579bf118db6c1ca20492b9af4228a414edd25f1006baace220e4ca771288e0f38d6cbf253ae4e9d39aaf4b881600b0d65e518e7d94891837ee9a0c6a723aac0f4d2b7bf4a65b36bd1fe636aa49c632"
                ),
                EarlycardInfo(
                    no = 2,
                    title = "영국 테이트 미술관 특별전2",
                    visitDate = "2021.12.11",
                    imgUrl = "https://img.insight.co.kr/static/2020/06/15/700/h4ubvce2f6vz4znnbnmw.jpg"
                ),
                EarlycardInfo(
                    no = 3,
                    title = "영국 테이트 미술관 특별전3",
                    visitDate = "2021.12.13",
                    imgUrl = "http://img.insight.co.kr/static/2018/09/05/700/76h47gh8f60w6dj2juz0.jpg"
                ),
                EarlycardInfo(
                    no = 4,
                    title = "영국 테이트 미술관 특별전4",
                    visitDate = "2021.12.15",
                    imgUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT-BVmcBn49BMhZelpzwCZe1n9if6tLs-6Mwg&usqp=CAU"
                ),
                EarlycardInfo(
                    no = 5,
                    title = "영국 테이트 미술관 특별전5",
                    visitDate = "2021.12.17",
                    imgUrl = "https://file.mk.co.kr/meet/neds/2012/11/image_readtop_2012_761907_1353239300779199.jpg"
                )
            )
        )
    }

    private fun initToken() = viewModelScope.launch {
        // Token 정보 초기화
        token = repository.getPreferenceTokenFlow().first()
    }
}