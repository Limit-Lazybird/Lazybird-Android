package com.limit.lazybird.ui.calendaradd

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.data.PreferenceDataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarAddViewModel @Inject constructor(
    private val repository: CalendarAddRepository
) : ViewModel() {

    companion object {
        private val TAG = "CalendarAddViewModel"
    }

    lateinit var token: String

    init {
        initToken()
    }

    private fun initToken() = viewModelScope.launch {
        token = repository.getPreferenceFlow().first()
    }

    fun saveCalendarInfo(
        exhbt_cd: String,
        reser_dt: String,
        start_time: String,
        end_time: String
    ) = viewModelScope.launch {
        repository.saveCalendarInfo(token, exhbt_cd, reser_dt, start_time, end_time)
    }

    fun deleteCalendarInfo(
        exhbt_cd: String,
    ) = viewModelScope.launch {
        Log.e("test", token)
        Log.e("test", exhbt_cd)
        repository.deleteCalendarInfo(token, exhbt_cd)
    }

    fun saveCustomInfo(
        exhbt_nm: String,
        exhbt_lct: String,
        reser_dt: String,
        start_time: String,
        end_time: String
    ) = viewModelScope.launch {
        repository.saveCustomInfo(token, exhbt_nm, exhbt_lct, reser_dt, start_time, end_time)
    }

}