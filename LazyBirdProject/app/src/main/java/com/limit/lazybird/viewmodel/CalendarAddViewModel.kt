package com.limit.lazybird.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.limit.lazybird.repository.CalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarAddViewModel @Inject constructor(
    private val repository: CalendarRepository
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

    fun updateCalendarInfo(
        exhbt_cd: String,
        reser_dt: String,
        start_time: String,
        end_time: String
    ) = viewModelScope.launch {
        repository.updateCalendarInfo(token, exhbt_cd, reser_dt, start_time, end_time)
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

    fun updateCustomInfo(
        exhbt_cd: String,
        exhbt_nm: String,
        exhbt_lct: String,
        reser_dt: String,
        start_time: String,
        end_time: String
    ) = viewModelScope.launch {
        repository.updateCustomInfo(token, exhbt_cd, exhbt_nm, exhbt_lct, reser_dt, start_time, end_time)
    }
}