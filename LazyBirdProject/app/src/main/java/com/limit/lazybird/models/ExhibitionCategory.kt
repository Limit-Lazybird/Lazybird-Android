package com.limit.lazybird.models

data class ExhibitionCategory(
    val categoryName: String,
    val categoryList: List<ExhibitionCategoryOption>
)
