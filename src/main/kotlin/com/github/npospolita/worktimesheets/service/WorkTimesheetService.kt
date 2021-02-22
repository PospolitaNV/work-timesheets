package com.github.npospolita.worktimesheets.service

import com.github.npospolita.worktimesheets.dao.EmployeeRepository
import com.github.npospolita.worktimesheets.dao.WorkTimesheetRepository
import com.github.npospolita.worktimesheets.utils.ReportFormatterUtils
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class WorkTimesheetService(
    private val workTimesheetRepository: WorkTimesheetRepository,
    private val employeeRepository: EmployeeRepository
) {
    companion object {
        val log = LoggerFactory.getLogger(WorkTimesheetService::class.java.name)
    }

    fun checkTimesheet(employeeId: Long): String {
        val employee = employeeRepository.findByIdOrNull(employeeId)
        log.info("Checking employee's timesheets:{}", employee)
        val notAccountedTimesheets =
            workTimesheetRepository.findAllByTakenIntoAccountFalseAndId_EmployeeId(employeeId)

        log.info("Not taken into account:{}", notAccountedTimesheets)

        if (notAccountedTimesheets.isEmpty())
            return ""

        val stringBuilder = StringBuilder()

        ReportFormatterUtils.addWorkReportHeader(stringBuilder, employee!!)

        for (day in notAccountedTimesheets) {
            ReportFormatterUtils.addWorkReportDayTimesheet(stringBuilder, day, null)
        }

        log.info(stringBuilder.toString())

        return stringBuilder.toString()
    }

    fun checkTimesheets(): String {
        val stringBuilder = StringBuilder()
        for (employee in employeeRepository.findAll()) {
            val timesheet = this.checkTimesheet(employee.id)
            if (!timesheet.isBlank()) stringBuilder.append(timesheet).append("\n\n");
        }
        return stringBuilder.toString()
    }

}