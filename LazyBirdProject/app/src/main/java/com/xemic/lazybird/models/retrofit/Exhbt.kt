package com.xemic.lazybird.models.retrofit

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Exhbt (
    val exhbt_cd: String,
    val exhbt_nm: String,
    val exhbt_sub_nm: String?,
    val exhbt_sn: String,
    val exhbt_lct: String,
    val exhbt_from_dt: String,
    val exhbt_to_dt: String,
    val exhbt_age: String,
    val exhbt_prc: String,
    val dc_percent: String?,
    val dc_prc: String?,
    val exhbt_expnt: String?,
    val dt_img: String,
    val excbt_url: String,
    val exhbt_notice: String?,
    val eb_yn: String,
    val exhbt_type_cd: Int,
    val like_yn: String
) : Parcelable {
}