package com.limit.lazybird.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EarlycardInfo(
    val no: Int,
    val title: String,
    val visitDate: String,
    val imgUrl: String
) : Parcelable
