package com.github.npospolita.worktimesheets.service

import com.github.kotlintelegrambot.Bot
import com.github.npospolita.worktimesheets.dao.WorkTimesheetRepository
import com.github.npospolita.worktimesheets.domain.CheckInType
import com.github.npospolita.worktimesheets.domain.WorkTimesheet
import com.github.npospolita.worktimesheets.domain.WorkTimesheetId
import com.github.npospolita.worktimesheets.domain.errors.ValidationError
import org.slf4j.LoggerFactory.getLogger
import org.springframework.stereotype.Service

@Service
class CheckInService(
        private val workTimesheetRepository: WorkTimesheetRepository,
        private val securityService: SecurityService,
        private val employeeService: EmployeeService,
        private val dateTimeProviderService: DateTimeProviderService
) {
    companion object {
        val log = getLogger(CheckInService::class.java.name)
    }

    fun checkIn(checkInType: CheckInType, employeeId: Long) {
        log.info("Received check_in: {} for employee: {}", checkInType, employeeId)

        val workTimesheet = workTimesheetRepository.findById(
                WorkTimesheetId(
                        dateTimeProviderService.getWorktimesheetDate(),
                        employeeId
                )
        )
                .orElse(createEmptyTimesheet(employeeId))

        fillCheckInFields(checkInType, workTimesheet)


        log.info("Saved work-timesheet: {}", workTimesheetRepository.save(workTimesheet))
    }

    private fun createEmptyTimesheet(employeeId: Long) = WorkTimesheet(
            WorkTimesheetId(dateTimeProviderService.getWorktimesheetDate(), employeeId)
    )

    private fun fillCheckInFields(
            checkInType: CheckInType,
            workTimesheet: WorkTimesheet
    ) {
        when (checkInType) {
            CheckInType.IN -> {
                if (workTimesheet.startTime != null)
                    throw ValidationError("Вы уже отмечали начало работы сегодня " + dateTimeProviderService.getWorktimesheetDate())

                workTimesheet.startTime = dateTimeProviderService.getWorktimesheetTime()
            }

            CheckInType.OUT -> {
                if (workTimesheet.startTime == null)
                    throw ValidationError("Вы ещё не отмечали начало работы сегодня " + dateTimeProviderService.getWorktimesheetDate())
                if (workTimesheet.endTime != null)
                    throw ValidationError("Вы уже отмечали конец работы сегодня " + dateTimeProviderService.getWorktimesheetDate())

                workTimesheet.endTime = dateTimeProviderService.getWorktimesheetTime()
            }
        }
    }

    fun notifyAdmin(checkInType: CheckInType, userId: Long, bot: Bot) {
        val employee = employeeService.getEmployee(userId)
        val actionType = if (checkInType == CheckInType.IN) "пришла!" else "ушла!"
        for (adminId in securityService.adminIdList) {
            bot.sendMessage(
                    adminId, text = """
            Работница "${employee.firstName} ${employee.lastName}" $actionType
        """.trimIndent()
            )
        }
    }


}