package com.limit.lazybird.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExhibitionInfo(
    val id: String,
    val title: String,
    val dDay: Int,
    val place: String,
    val startDate: String,
    val endDate: String,
    val discount: Int,
    val price: Int,
    val discountedPrice: Int,
    val notice: String,
    val thumbnailImageUrl: String,
    val exhibitionDetailImgUrl: String,
    val exhibitionUrl: String,
    val like_yn: Boolean
) : Parcelable
