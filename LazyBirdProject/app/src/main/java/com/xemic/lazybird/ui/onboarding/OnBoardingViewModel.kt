package com.xemic.lazybird.ui.onboarding

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xemic.lazybird.custom.OptionItemView
import com.xemic.lazybird.models.ExhibitionCategory
import com.xemic.lazybird.models.ExhibitionCategoryOption
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel

/*** deprecated ***/

class OnBoardingViewModel @Inject constructor(
    private val repository: OnBoardingRepository
) : ViewModel() {
    val exhibitionCategories = MutableLiveData<List<ExhibitionCategory>>()

    init {
        getExhibitionCategories()
    }

    private fun getExhibitionCategories() {
        // repository.getExhibitionCategories().let { response ->
        //     exhibitionCategories.postValue(response.body())
        // }

        // Todo : dummyData
        exhibitionCategories.postValue(
            listOf(
                ExhibitionCategory(
                    "카테고리 1",
                    listOf(
                        ExhibitionCategoryOption("옵션선택1"),
                        ExhibitionCategoryOption("옵션선택2"),
                        ExhibitionCategoryOption("옵션선택선택3"),
                        ExhibitionCategoryOption("션옵선택4"),
                        ExhibitionCategoryOption("택선션옵5")
                    )
                ),
                ExhibitionCategory(
                    "카테고리 2",
                    listOf(
                        ExhibitionCategoryOption("이미옵션선택", true),
                        ExhibitionCategoryOption("오옵션선택2"),
                        ExhibitionCategoryOption("옵션선택삼번"),
                        ExhibitionCategoryOption("옵션선택_아무말"),
                        ExhibitionCategoryOption("옵션선택_아무말"),
                        ExhibitionCategoryOption("옵션선택_아무말"),
                        ExhibitionCategoryOption("옵션선택_아무말"),
                        ExhibitionCategoryOption("옵션선택_아무말"),
                        ExhibitionCategoryOption("옵션선택_아무말"),
                        ExhibitionCategoryOption("옵션선택_아무말"),
                        ExhibitionCategoryOption("옵션선택_아무말"),
                        ExhibitionCategoryOption("옵션선택_아무말")
                    )
                )
            )
        )
    }

    fun optionItemClicked(view: View, position: Int, idx: Int) {
        if (exhibitionCategories.value != null) {
            // Model update
            exhibitionCategories.value!![position].categoryList[idx].isSelected =
                !exhibitionCategories.value!![position].categoryList[idx].isSelected
            // View update
            (view as OptionItemView).also {
                it.isSelected = !it.isSelected
            }
        }
    }
}