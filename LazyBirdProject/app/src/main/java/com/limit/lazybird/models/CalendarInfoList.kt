package com.limit.lazybird.models

import android.os.Parcelable
import com.limit.lazybird.models.retrofit.CalendarInfo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CalendarInfoList(
    val calendarInfoList: List<CalendarInfo>
): Parcelable