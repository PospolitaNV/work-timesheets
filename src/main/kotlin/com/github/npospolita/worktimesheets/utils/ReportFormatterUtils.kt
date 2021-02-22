package com.github.npospolita.worktimesheets.utils

import com.github.npospolita.worktimesheets.domain.Employee
import com.github.npospolita.worktimesheets.domain.WorkTimesheet
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ReportFormatterUtils {

    fun addWorkReportDayTimesheet(textSummary: StringBuilder, day: WorkTimesheet, timeDiff: Long?) {
        textSummary.append("${formatDate(day.id.day)}: ${formatTime(day.startTime)} - ${formatTime(day.endTime) ?: "???"}")
        timeDiff?.let { textSummary.append(", ${timeDiff}мин.\n") }
    }

    private fun formatDate(day: LocalDate?): String? {
        return day?.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    }

    private fun formatTime(time: LocalDateTime?): String? {
        return time?.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    fun addWorkReportHeader(textSummary: StringBuilder, employee: Employee) {
        textSummary.append("Работник \"${employee.firstName} ${employee.lastName}\" отработал(а):\n")
    }

    fun addWorkReportFooter(textSummary: StringBuilder, timeSummary: Long, salary: Int?) {
        textSummary.append("_____________________\n")
        textSummary.append("Всего отработано: ${timeSummary}мин.")

        salary?.let { textSummary.append(", зарплата: ${salary}р\n\n") }
    }
}