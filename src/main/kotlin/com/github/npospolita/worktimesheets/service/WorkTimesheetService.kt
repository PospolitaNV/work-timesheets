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

    fun checkTimesheets(employeeId: Long): String {
        val employee = employeeRepository.findByIdOrNull(employeeId)
        log.info("Checking employee's timesheets:{}", employee)
        val notAccountedTimesheets =
            workTimesheetRepository.findAllByTakenIntoAccountFalseAndId_EmployeeId(employeeId)

        log.info("Not taken into account:{}", notAccountedTimesheets)
        val stringBuilder = StringBuilder()

        ReportFormatterUtils.addWorkReportHeader(stringBuilder, employee!!)

        notAccountedTimesheets.stream().map { day ->
            ReportFormatterUtils.addWorkReportDayTimesheet(stringBuilder, day, null)
        }
        log.info("Employee's {} timesheets:", employee)
        log.info(stringBuilder.toString())

        return stringBuilder.toString()
    }

    fun checkTimesheets(): String {
        val stringBuilder = StringBuilder()
        for (employee in employeeRepository.findAll()) {
            stringBuilder.append(this.checkTimesheets(employee.id)).append("\n\n");
        }
        return stringBuilder.toString()
    }

}