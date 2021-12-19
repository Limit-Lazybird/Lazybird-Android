package com.limit.lazybird.models

import java.time.LocalDate

data class DaySchedule(
    val scheduleDate: LocalDate,
    val schedules: List<Schedule>
)
