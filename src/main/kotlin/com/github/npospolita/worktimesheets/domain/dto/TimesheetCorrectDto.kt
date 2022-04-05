package com.github.npospolita.worktimesheets.domain.dto

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class TimesheetCorrectDto(
    @field:DateTimeFormat(pattern = "yyyy-MM-dd") val workDay: LocalDate,
    val employeeId: Long,
    val firstName: String,
    val lastName: String,
    @field:DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") val startTime: LocalDateTime,
    @field:NotBlank val endTime: String?
)
