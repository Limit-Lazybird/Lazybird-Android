package com.xemic.lazybird.util

import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil
import android.view.View
import com.kizitonwose.calendarview.model.CalendarDay
import java.time.ZoneId

// 12345 >> 12,345
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

// "yyyy-MM-dd" to Date
fun String.toDate(): Date = SimpleDateFormat("yyyy-MM-dd").parse(this)

// "yyyy.MM.dd" to Date
fun String.toDate2(): Date = SimpleDateFormat("yyyy.MM.dd").parse(this)

// CalendarDay to Date
fun CalendarDay.toDate(): Date = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())

// \\n to \n
fun String.applyEscapeSequence(): String {
    return this.replace("\\n", "\n")
}

// "yyyy.MM.dd" to "yyyy-MM-dd"
fun String.dateFormatted(): String = SimpleDateFormat("yyyy.MM.dd").format(
    SimpleDateFormat("yyyy-MM-dd").parse(this)
)

// calculate date diff of d1, d2
fun calculateDateDiff(d1: Date, d2: Date): Int {
    return if (d1.time > d2.time) {
        ceil(((d1.time - d2.time) / (1000 * 60 * 60 * 24f))).toInt()
    } else {
        ceil(((d2.time - d1.time) / (1000 * 60 * 60 * 24f))).toInt()
    }
}

fun Float.toDp(view: View) =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        20f,
        view.resources.displayMetrics
    ).toInt()

fun Date.parseDay() = getCalendar(this).get(Calendar.DAY_OF_MONTH)
fun Date.parseMonth() = getCalendar(this).get(Calendar.MONTH)
fun Date.parseDayOfWeek() = getCalendar(this).get(Calendar.DAY_OF_WEEK)

fun getCalendar(date: Date) = Calendar.getInstance().apply {
    time = date
}
