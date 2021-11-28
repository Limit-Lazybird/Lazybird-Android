package com.xemic.lazybird.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xemic.lazybird.models.retrofit.CustomInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/************* OnbViewModel ***************
 * 온보딩 시작화면 >> 온보딩 화면 (ViewModel)
 * 온보딩 설문조사하는 화면
 ********************************************** ***/
@HiltViewModel
class OnbViewModel @Inject constructor(
    private val repository: OnbRepository
) : ViewModel() {

    companion object {
        const val TAG = "OnbViewModel"
    }

    private lateinit var token: String
    private val _customizedList = MutableLiveData<List<CustomInfo>>()
    val customizedList : LiveData<List<CustomInfo>> get() = _customizedList

    private val _page = MutableLiveData(0)
    var page: LiveData<Int> = _page
    var selectedResult = mutableListOf<Int>() // page 별로 선택한 답변 리스트

    init {
        initToken()
        getCustomizedList()
    }

    private fun initToken() = viewModelScope.launch {
        // dataStore 에서 토큰 값 가져오기
        token = repository.getPreferenceFlow().first()
    }

    private fun getCustomizedList() = viewModelScope.launch {
        // 질문 정보 가져오기
        val response = repository.getCustomizedList(token)
        response.body()?.let{
            _customizedList.postValue(it.customList)
        }
    }

    fun initSelectedResult(size:Int) {
        // size 크기에 맞게 selectedResult 초기화
        selectedResult = MutableList(size) { -1 }
    }

    fun movePrevPage() {
        // 이전 페이지로 이동
        _page.value = page.value!! - 1
    }

    fun moveNextPage() {
        // 다음 페이지로 이동
        _page.value = page.value!! + 1
    }

    fun clickOptionBox(page: Int, selectedNo: Int) {
        // 답변 선택
        selectedResult[page] = selectedNo
    }

    fun insertCustomizedList(answer_idx: String) = viewModelScope.launch {
        // 내 맞춤전시 정보 등록
        repository.insertCustomizedList(token, answer_idx)
    }

    fun deleteCustomizedList() = viewModelScope.launch {
        // 내 맞춤전시 정보 제거
        repository.deleteCustomizedList(token)
    }
}