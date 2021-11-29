package com.limit.lazybird.models

import java.util.*

data class Schedule(
    val date: Date,
    val scheduleName: String,
    val schedulePlace: String,
    val startTime: String,
    val endTime: String,
    val isVisited: Boolean = false
)
