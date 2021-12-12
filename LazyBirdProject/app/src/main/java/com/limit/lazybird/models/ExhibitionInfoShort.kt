package com.limit.lazybird.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExhibitionInfoShort(
    val id: String,
    val title: String,
    val place: String,
    val startDate: String,
    val endDate: String,
    val discount: Int,
    val price: Int,
    val discountedPrice: Int,
    val thumbnailImageUrl: String,
    val isLiked: Boolean
) : Parcelable
