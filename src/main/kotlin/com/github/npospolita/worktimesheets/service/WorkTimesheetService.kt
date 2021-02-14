package com.github.npospolita.worktimesheets.service

import com.github.npospolita.worktimesheets.dao.EmployeeRepository
import com.github.npospolita.worktimesheets.dao.WorkTimesheetRepository
import com.github.npospolita.worktimesheets.utils.ReportFormatterUtils
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.temporal.ChronoUnit

@Service
class WorkTimesheetService(
    val workTimesheetRepository: WorkTimesheetRepository,
    val employeeRepository: EmployeeRepository
){
    companion object {
        val log = LoggerFactory.getLogger(WorkTimesheetService::class.java.name)
    }

    fun checkTimesheets(employeeId: Long) : String {
        val employee = employeeRepository.findByIdOrNull(employeeId)
        log.info("Checking employee's timesheets:{}", employee)
        val notAccountedTimesheets =
            workTimesheetRepository.findAllByTakenIntoAccountFalseAndId_EmployeeId(employeeId)

        val stringBuilder = StringBuilder()

        ReportFormatterUtils.addWorkReportHeader(stringBuilder, employee!!)

        notAccountedTimesheets.stream().map { day ->
            ReportFormatterUtils.addWorkReportDayTimesheet(stringBuilder, day, null)
        }
        log.info("Employee's {} timesheets:", employee)
        log.info(stringBuilder.toString())

        return stringBuilder.toString()
    }

}