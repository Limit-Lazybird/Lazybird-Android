package com.xemic.lazybird.util

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

fun Int.thousandUnitFormatted(): String {
    val str = this.toString().reversed()
    var outStr = ""
    for (idx in str.indices) {
        outStr += "${str[idx]}"
        if (idx % 3 == 2)
            outStr += ","
    }
    return outStr.reversed()
}

fun String.toDate(): Date = SimpleDateFormat("yyyy-MM-dd").parse(this)
fun String.toDate2(): Date = SimpleDateFormat("yyyy.MM.dd").parse(this)
fun String.applyEscapeSequence(): String {
    return this.replace("\\n", "\n")
}
fun String.dateFormatted(): String = SimpleDateFormat("yyyy.MM.dd").format(
    SimpleDateFormat("yyyy-MM-dd").parse(this)
)

fun calculateDateDiff(d1: Date, d2: Date): Int {
    return if (d1.time > d2.time) {
        ceil(((d1.time - d2.time) / (1000 * 60 * 60 * 24f))).toInt()
    } else {
        ceil(((d2.time - d1.time) / (1000 * 60 * 60 * 24f))).toInt()
    }
}
