package com.limit.lazybird.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Answer(
    val answerTitle: String,
    val answerImgUrl: String = "",
) : Parcelable
