package com.github.npospolita.worktimesheets.service

import com.github.npospolita.worktimesheets.DatabaseTestBase
import com.github.npospolita.worktimesheets.dao.WorkReportRepository
import com.github.npospolita.worktimesheets.dao.WorkTimesheetRepository
import com.github.npospolita.worktimesheets.domain.Employee
import com.github.npospolita.worktimesheets.domain.WorkTimesheet
import com.github.npospolita.worktimesheets.domain.WorkTimesheetId
import com.github.npospolita.worktimesheets.domain.errors.ValidationError
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.transaction.Transactional

@SpringBootTest
@Transactional
class WorkReportServiceTest(
    @Autowired val workTimesheetRepository: WorkTimesheetRepository,
    @Autowired val workReportRepository: WorkReportRepository,
    @Autowired val employeeService: EmployeeService,
    @Autowired val workReportService: WorkReportService
) : DatabaseTestBase() {

    @Test
    fun makeReport() {
        employeeService.addEmployee(Employee(1L, "Kekes", "Maximus", 100))

        saveTimesheet(10, 0, 20, 20, 1, false)
        saveTimesheet(10, 0, 20, 20, 2, false)
        saveTimesheet(10, 0, 20, 20, 3, false)
        saveTimesheet(10, 0, 20, 20, 4, false)
        saveTimesheet(10, 0, 20, 20, 5, false)

        workReportService.makeReport(1L)

        val reports = workReportRepository.findAllByEmployeeId(1L)
        assertEquals(1, reports.size)
        assertEquals(BigInteger.valueOf(5166L), reports[0].amount)
        assertEquals(
            listOf(
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2021, 1, 2),
                LocalDate.of(2021, 1, 3),
                LocalDate.of(2021, 1, 4),
                LocalDate.of(2021, 1, 5)
            ), reports[0].days
        )
    }

    @Test
    fun makeReportTwice() {
        employeeService.addEmployee(Employee(1L, "Kekes", "Maximus", 100))

        saveTimesheet(10, 0, 20, 20, 1, false)
        saveTimesheet(10, 0, 20, 20, 2, false)
        saveTimesheet(10, 0, 20, 20, 3, false)
        saveTimesheet(10, 0, 20, 20, 4, false)
        saveTimesheet(10, 0, 20, 20, 5, false)

        workReportService.makeReport(1L)

        var reports = workReportRepository.findAllByEmployeeId(1L)
        assertEquals(1, reports.size)
        assertEquals(BigInteger.valueOf(5166L), reports[0].amount)
        assertEquals(
            listOf(
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2021, 1, 2),
                LocalDate.of(2021, 1, 3),
                LocalDate.of(2021, 1, 4),
                LocalDate.of(2021, 1, 5)
            ), reports[0].days
        )

        workReportService.makeReport(1L)
        reports = workReportRepository.findAllByEmployeeId(1L)
        assertEquals(1, reports.size)
    }

    @Test
    fun makeReportWithNotEndedDate() {
        employeeService.addEmployee(Employee(1L, "Kekes", "Maximus", 100))

        saveTimesheet(10, 0, 20, 20, 1, false)
        saveTimesheet(10, 0, 20, 20, 2, false)
        saveTimesheet(10, 0, 20, 20, 3, false)
        workTimesheetRepository.save(
            WorkTimesheet(
                WorkTimesheetId(LocalDate.now(), 1L),
                startTime = LocalDateTime.now(),
                endTime = null,
                takenIntoAccount = false
            )
        )
        saveTimesheet(10, 0, 20, 20, 5, false)

        assertThrows<ValidationError> {
            workReportService.makeReport(1L)
        }
    }

    @Test
    fun makeReportWithNotStartedDate() {
        employeeService.addEmployee(Employee(1L, "Kekes", "Maximus", 100))

        saveTimesheet(10, 0, 20, 20, 1, false)
        saveTimesheet(10, 0, 20, 20, 2, false)
        saveTimesheet(10, 0, 20, 20, 3, false)
        workTimesheetRepository.save(
            WorkTimesheet(
                WorkTimesheetId(LocalDate.now(), 1L),
                startTime = null,
                endTime = LocalDateTime.now(),
                takenIntoAccount = false
            )
        )
        saveTimesheet(10, 0, 20, 20, 5, false)

        assertThrows<ValidationError> {
            workReportService.makeReport(1L)
        }
    }

    @Test
    fun makeEmptyReport() {
        employeeService.addEmployee(Employee(1L, "Kekes", "Maximus", 100))

        workReportService.makeReport(1L)

        val reports = workReportRepository.findAllByEmployeeId(1L)
        assertEquals(0, reports.size)
    }

    protected fun saveTimesheet(
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        dayOfMonth: Int,
        taken: Boolean
    ) {
        val day = LocalDate.of(2021, 1, dayOfMonth)
        workTimesheetRepository.save(
            WorkTimesheet(
                WorkTimesheetId(day, 1L),
                startTime = LocalDateTime.of(day, LocalTime.of(startHour, startMinute)),
                endTime = LocalDateTime.of(day, LocalTime.of(endHour, endMinute)),
                takenIntoAccount = taken
            )
        )
    }

}