package com.limit.lazybird.ui.calendaradd

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
    private val apiHelper: ApiHelper,
    private val dataStoreManager: PreferenceDataStoreManager
) : ViewModel() {

    companion object {
        private val TAG = "CalendarAddViewModel"
    }

    lateinit var token: String

    init {
        initToken()
    }

    private fun initToken() = viewModelScope.launch {
        token = dataStoreManager.preferenceTokenFlow.first()
    }
}