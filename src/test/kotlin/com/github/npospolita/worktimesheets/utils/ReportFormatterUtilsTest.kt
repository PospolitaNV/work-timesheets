package com.github.npospolita.worktimesheets.utils

import com.github.npospolita.worktimesheets.domain.WorkTimesheet
import com.github.npospolita.worktimesheets.domain.WorkTimesheetId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class ReportFormatterUtilsTest {

    @Test
    fun addWorkReportDayTimesheet() {
        val stringBuilder = StringBuilder()
        ReportFormatterUtils.addWorkReportDayTimesheet(
            stringBuilder, WorkTimesheet(
                WorkTimesheetId(LocalDate.of(2020, 1, 1), 1L),
                startTime = LocalDate.of(2020, 1, 1).atTime(0, 0)
            )
        )

        assertEquals("01-01-2020: 00:00 - ???\n", stringBuilder.toString())
    }
}