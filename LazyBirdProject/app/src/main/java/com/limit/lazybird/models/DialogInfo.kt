package com.limit.lazybird.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DialogInfo(
    val title: String,
    val message: String,
    val positiveBtnTitle: String,
    val negativeBtnTitle: String
): Parcelable
