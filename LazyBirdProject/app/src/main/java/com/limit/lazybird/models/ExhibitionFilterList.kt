package com.limit.lazybird.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExhibitionFilterList(
    var exhibitionClassSelectedList: List<Int>,
    var exhibitionEtcSelectedList: List<Int>,
    var exhibitionPlaceSelectedList: List<Int>,
) : Parcelable
