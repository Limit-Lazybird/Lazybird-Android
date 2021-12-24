package com.limit.lazybird.models.retrofit

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CalendarInfo(
    val exhbt_cd: Int,
    val exhbt_nm: String,
    val exhbt_lct: String,
    val reser_dt: String,
    val start_time: String,
    val end_time: String,
    val visit_yn: String
): Parcelable {
    constructor() : this(0,"","","","","","")
}
