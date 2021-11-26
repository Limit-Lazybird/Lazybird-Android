package com.xemic.lazybird.ui.ticketing

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xemic.lazybird.api.ApiHelper
import com.xemic.lazybird.data.PreferenceDataStoreManager
import com.xemic.lazybird.models.ExhibitionInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketingViewModel @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
):ViewModel() {

    companion object {
        const val EXHIBITION_INFO = "exhibition_info"
    }

    private val _exhibitionInfo = MutableLiveData<ExhibitionInfo>()
    val exhibitionInfo: LiveData<ExhibitionInfo> get() = _exhibitionInfo

    private lateinit var token: String

    init {
        initToken()
    }

    private fun initToken() = viewModelScope.launch {
        token = dataStoreManager.preferenceTokenFlow.first()
    }

    fun updateExhibitionInfo(exhibitionInfo: ExhibitionInfo){
        _exhibitionInfo.postValue(exhibitionInfo)
    }

    fun updateExhibitionReservation() = viewModelScope.launch {
        apiHelper.exhbtReservationSave(token, exhibitionInfo.value!!.id)
    }
}