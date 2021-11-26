package com.xemic.lazybird.ui.onboarding

import com.xemic.lazybird.api.ApiHelper
import com.xemic.lazybird.models.ExhibitionCategory
import kotlinx.coroutines.coroutineScope
import retrofit2.Response
import javax.inject.Inject

/*** deprecated ***/

class OnBoardingRepository @Inject constructor(
    private val apiHelper: ApiHelper
) {
    // suspend fun getExhibitionCategories() = apiHelper.getExhibitionCategories()
}