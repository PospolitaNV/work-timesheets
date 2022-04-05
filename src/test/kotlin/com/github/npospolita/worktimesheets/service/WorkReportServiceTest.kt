package com.github.npospolita.worktimesheets.service

import com.github.npospolita.worktimesheets.DatabaseTestBase
import com.github.npospolita.worktimesheets.dao.WorkReportRepository
import com.github.npospolita.worktimesheets.dao.WorkTimesheetRepository
import com.github.npospolita.worktimesheets.domain.Employee
import com.github.npospolita.worktimesheets.domain.WorkTimesheet
import com.github.npospolita.worktimesheets.domain.WorkTimesheetId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
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

        saveTimesheet(10, 0, 20, 20, 1, false, 1L)
        saveTimesheet(10, 0, 20, 20, 2, false, 1L)
        saveTimesheet(10, 0, 20, 20, 3, false, 1L)
        saveTimesheet(10, 0, 20, 20, 4, false, 1L)
        saveTimesheet(10, 0, 20, 20, 5, false, 1L)

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

        saveTimesheet(10, 0, 20, 20, 1, false, 1L)
        saveTimesheet(10, 0, 20, 20, 2, false, 1L)
        saveTimesheet(10, 0, 20, 20, 3, false, 1L)
        saveTimesheet(10, 0, 20, 20, 4, false, 1L)
        saveTimesheet(10, 0, 20, 20, 5, false, 1L)

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

        saveTimesheet(10, 0, 20, 20, 1, false, 1L)
        saveTimesheet(10, 0, 20, 20, 2, false, 1L)
        saveTimesheet(10, 0, 20, 20, 3, false, 1L)
        workTimesheetRepository.save(
            WorkTimesheet(
                WorkTimesheetId(LocalDate.now(), 1L),
                startTime = LocalDateTime.now(),
                endTime = null,
                takenIntoAccount = false
            )
        )
        saveTimesheet(10, 0, 20, 20, 5, false, 1L)

        assertTrue(
            workReportService.makeReport(1L)!!
                .contains("Какая-то из дат сотрудника не имеет времени окончания или начала.")
        )
    }

    @Test
    fun makeReportWithNotStartedDate() {
        employeeService.addEmployee(Employee(1L, "Kekes", "Maximus", 100))

        saveTimesheet(10, 0, 20, 20, 1, false, 1L)
        saveTimesheet(10, 0, 20, 20, 2, false, 1L)
        saveTimesheet(10, 0, 20, 20, 3, false, 1L)
        workTimesheetRepository.save(
            WorkTimesheet(
                WorkTimesheetId(LocalDate.now(), 1L),
                startTime = null,
                endTime = LocalDateTime.now(),
                takenIntoAccount = false
            )
        )
        saveTimesheet(10, 0, 20, 20, 5, false, 1L)


        assertTrue(
            workReportService.makeReport(1L)!!
                .contains("Какая-то из дат сотрудника не имеет времени окончания или начала.")
        )

    }

    @Test
    fun makeEmptyReport() {
        employeeService.addEmployee(Employee(1L, "Kekes", "Maximus", 100))

        workReportService.makeReport(1L)

        val reports = workReportRepository.findAllByEmployeeId(1L)
        assertEquals(0, reports.size)
    }

    @Test
    fun makeReportsWhenOneOfEmployeesHaveInvalidTimesheet() {
        employeeService.addEmployee(Employee(1L, "Kekes", "Maximus", 100))
        employeeService.addEmployee(Employee(2L, "NotKekes", "NotMaximus", 100))

        saveTimesheet(10, 0, 20, 20, 1, false, 1L)
        saveTimesheet(10, 0, 20, 20, 2, false, 1L)
        saveTimesheet(10, 0, 20, 20, 4, false, 2L)
        saveTimesheet(10, 0, 20, 20, 5, false, 2L)
        workTimesheetRepository.save(
            WorkTimesheet(
                WorkTimesheetId(LocalDate.now(), 2L),
                startTime = LocalDateTime.now(),
                endTime = null,
                takenIntoAccount = false
            )
        )
        var result = workReportService.makeAllReports()
        assertTrue(result.contains("Kekes Maximus"))
        assertTrue(result.contains("NotKekes NotMaximus"))
        assertTrue(result.contains("Какая-то из дат сотрудника не имеет времени окончания или начала."))

        result = workReportService.makeAllReports()
        assertTrue(result.contains("Какая-то из дат сотрудника не имеет времени окончания или начала."))
        assertTrue(result.contains("NotKekes NotMaximus"))
        assertFalse(result.contains("Kekes Maximus"))
    }

    @Test
    fun makeReportWithTimeBetweenTwoDates() {
        employeeService.addEmployee(Employee(1L, "Kekes", "Maximus", 100))

        val day = LocalDate.of(2021, 1, 1)
        workTimesheetRepository.save(
            WorkTimesheet(
                WorkTimesheetId(day, 1L),
                startTime = LocalDateTime.of(day, LocalTime.of(10, 10)),
                endTime = LocalDateTime.of(day.plusDays(1), LocalTime.of(5, 0)),
                takenIntoAccount = false
            )
        )

        workReportService.makeReport(1L)

        val reports = workReportRepository.findAllByEmployeeId(1L)
        assertEquals(1, reports.size)
        assertEquals(BigInteger.valueOf(1883L), reports[0].amount)
        assertEquals(
            listOf(
                LocalDate.of(2021, 1, 1),
            ), reports[0].days
        )
    }

    fun saveTimesheet(
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        dayOfMonth: Int,
        taken: Boolean,
        employeeId: Long
    ) {
        val day = LocalDate.of(2021, 1, dayOfMonth)
        workTimesheetRepository.save(
            WorkTimesheet(
                WorkTimesheetId(day, employeeId),
                startTime = LocalDateTime.of(day, LocalTime.of(startHour, startMinute)),
                endTime = LocalDateTime.of(day, LocalTime.of(endHour, endMinute)),
                takenIntoAccount = taken
            )
        )
    }

}
