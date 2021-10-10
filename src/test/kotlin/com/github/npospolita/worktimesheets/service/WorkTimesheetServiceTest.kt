package com.github.npospolita.worktimesheets.service

import com.github.npospolita.worktimesheets.DatabaseTestBase
import com.github.npospolita.worktimesheets.dao.WorkTimesheetRepository
import com.github.npospolita.worktimesheets.domain.Employee
import com.github.npospolita.worktimesheets.domain.WorkTimesheet
import com.github.npospolita.worktimesheets.domain.WorkTimesheetId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.transaction.Transactional


@SpringBootTest
@Transactional
class WorkTimesheetServiceTest(
    @Autowired val workTimesheetRepository: WorkTimesheetRepository,
    @Autowired val employeeService: EmployeeService,
    @Autowired val workTimesheetService: WorkTimesheetService
) : DatabaseTestBase() {

    @Test
    fun checkTimesheets() {
        employeeService.addEmployee(Employee(1L, "Kekes", "Maximus", 100))

        saveTimesheet(10, 0, 20, 20, 1, false)
        saveTimesheet(10, 0, 20, 20, 2, false)
        saveTimesheet(10, 0, 20, 20, 3, false)
        saveTimesheet(10, 0, 20, 20, 4, false)
        saveTimesheet(10, 0, 20, 20, 5, false)

        val checkTimesheet = workTimesheetService.checkTimesheet(1L)
        println(checkTimesheet)
    }

    @Test
    fun checkMonthlyReport() {
        employeeService.addEmployee(Employee(1L, "Kekes", "Maximus", 100))

        saveTimesheet(10, 0, 20, 20, 1, false)
        saveTimesheet(10, 0, 20, 20, 2, false)
        saveTimesheet(10, 0, 20, 20, 3, false)
        saveTimesheet(10, 0, 20, 20, 4, false)
        saveTimesheet(10, 0, 20, 20, 5, false)

        val checkTimesheet = workTimesheetService.monthlyReport(1L)
        println(checkTimesheet)
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
