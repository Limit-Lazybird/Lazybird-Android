package com.xemic.lazybird.models

import java.time.LocalDate

data class Schedule(
    val date: LocalDate,
    val scheduleName: String,
    val isVisited: Boolean = false
)
