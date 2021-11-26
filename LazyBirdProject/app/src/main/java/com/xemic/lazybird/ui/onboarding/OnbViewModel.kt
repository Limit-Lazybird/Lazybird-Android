package com.xemic.lazybird.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xemic.lazybird.models.Answer
import com.xemic.lazybird.models.Survey
import com.xemic.lazybird.models.retrofit.CustomInfo
import com.xemic.lazybird.models.retrofit.Exhbt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnbViewModel @Inject constructor(
    private val repository: OnbStartRepository
) : ViewModel() {

    private lateinit var token: String
    private val _customizedList = MutableLiveData<List<CustomInfo>>()
    val customizedList : LiveData<List<CustomInfo>> get() = _customizedList

    private val _page = MutableLiveData(0)
    var page: LiveData<Int> = _page
    var selectedResult = mutableListOf<Int>()

    init {
        viewModelScope.launch {
            getCustomizedList()
            initToken()
        }
    }

    private fun initToken() = viewModelScope.launch {
        token = repository.getPreferenceFlow().first()
    }

    private suspend fun getCustomizedList() = viewModelScope.launch {
        val token = repository.getPreferenceFlow().first()
        val response = repository.getCustomizedList(token)
        response.body()?.let{
            _customizedList.postValue(it.customList)
        }
    }

    fun initSelectedResult(size:Int) {
        selectedResult = MutableList(size) { -1 }
    }

    fun movePrevPage() {
        _page.value = page.value!! - 1
    }

    fun moveNextPage() {
        _page.value = page.value!! + 1
    }

    fun clickOptionBox(page: Int, selectedNo: Int) {
        selectedResult[page] = selectedNo
    }

    fun insertCustomizedList(answer_idx: String) = viewModelScope.launch {
        repository.insertCustomizedList(token, answer_idx)
    }

    fun deleteCustomizedList() = viewModelScope.launch {
        repository.deleteCustomizedList(token)
    }
}