package com.limit.lazybird.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DialogResult(
    val results: List<String>
): Parcelable
