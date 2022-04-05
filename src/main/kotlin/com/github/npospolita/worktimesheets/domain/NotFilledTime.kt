package com.github.npospolita.worktimesheets.domain

import java.time.LocalDate
import java.time.LocalDateTime

interface NotFilledTime {
    val workDay: LocalDate
    val employeeId: Long
    val firstName: String
    val lastName: String
    val startTime: LocalDateTime
    val endTime: LocalDateTime?
}
