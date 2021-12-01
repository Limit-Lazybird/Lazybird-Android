package com.limit.lazybird.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Survey(
    val question: String,
    val answerList: List<Answer>
) : Parcelable
