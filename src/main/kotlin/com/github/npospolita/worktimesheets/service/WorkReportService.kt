package com.github.npospolita.worktimesheets.service

import com.github.npospolita.worktimesheets.dao.EmployeeRepository
import com.github.npospolita.worktimesheets.dao.WorkReportRepository
import com.github.npospolita.worktimesheets.dao.WorkTimesheetRepository
import com.github.npospolita.worktimesheets.domain.WorkReport
import com.github.npospolita.worktimesheets.domain.errors.ValidationError
import com.github.npospolita.worktimesheets.utils.ReportFormatterUtils
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*
import javax.transaction.Transactional
import kotlin.collections.ArrayList

@Service
class WorkReportService(
    private val workReportRepository: WorkReportRepository,
    private val workTimesheetRepository: WorkTimesheetRepository,
    private val employeeRepository: EmployeeRepository
) {
    companion object {
        val log = LoggerFactory.getLogger(WorkReportService::class.java.name)
    }

    @Transactional
    fun makeReport(employeeId: Long): String {
        val employee = employeeRepository.findByIdOrNull(employeeId)
        log.info("Starting to calculate employee's salary:{}", employee)

        val notAccountedTimesheets = workTimesheetRepository.findAllByTakenIntoAccountFalseAndId_EmployeeId(employeeId)

        if (notAccountedTimesheets.isEmpty()) {
            log.info("Employee's {} timesheets are empty.", employee)
            return "У этого сотрудника ещё нет неоплаченных записей о работе"
        }

        val workingDays = ArrayList<LocalDate>()
        var timeSummary = 0L
        val textSummary = StringBuilder()

        ReportFormatterUtils.addWorkReportHeader(textSummary, employee!!)

        for (day in notAccountedTimesheets) {
            if (day.endTime == null || day.startTime == null) throw ValidationError("Какая-то из дат сотрудника не имеет времени окончания или начала.")

            val timeDiff = ChronoUnit.MINUTES.between(day.startTime, day.endTime)
            ReportFormatterUtils.addWorkReportDayTimesheet(textSummary, day, timeDiff)
            timeSummary += timeDiff
            workingDays.add(day.id.day)
            day.takenIntoAccount = true
        }

        val salary = BigDecimal(employee.wage.toLong()) // зп за час
            .divide(BigDecimal(60), 20, RoundingMode.HALF_UP) // зп в минуту
            .multiply(BigDecimal(timeSummary), MathContext(20, RoundingMode.HALF_UP)) // минуты за период
            .toBigInteger()

        ReportFormatterUtils.addWorkReportFooter(textSummary, timeSummary, salary.toInt())

        workTimesheetRepository.saveAll(notAccountedTimesheets)
        workReportRepository.save(WorkReport(UUID.randomUUID(), employeeId, workingDays, salary))

        log.info("Saved employee's {} salary:{}", employee, salary)
        log.info(textSummary.toString())

        return textSummary.toString()
    }

    fun makeAllReports() {
        for (employee in employeeRepository.findAll()) {
            this.makeReport(employee.id)
        }
    }


    fun makeReport(employeeFullName: String): String {
        val firstAndLastName = employeeFullName.split(" ")
        if (firstAndLastName.size != 2) throw ValidationError("Введите правильные Фамилию и Имя пользователя")
        val employee =
            employeeRepository.findByFirstNameAndLastName(firstAndLastName[0], firstAndLastName[1])
                .orElseThrow { ValidationError("Введите правильные Фамилию и Имя пользователя") }
        return makeReport(employee.id)
    }

}