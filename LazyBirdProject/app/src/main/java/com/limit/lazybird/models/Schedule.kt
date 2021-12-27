package com.limit.lazybird.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Schedule(
    val id: Int,
    val date: Date,
    val scheduleName: String,
    val schedulePlace: String,
    val startTime: String,
    val endTime: String,
    val isCustom: Boolean,
    val isVisited: Boolean = false
): Parcelable {
    constructor() : this(0,Date(),"","","","",false,false)
}
