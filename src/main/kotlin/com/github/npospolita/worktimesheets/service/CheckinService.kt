package com.github.npospolita.worktimesheets.service

import com.github.npospolita.worktimesheets.dao.WorkTimesheetRepository
import com.github.npospolita.worktimesheets.domain.CheckInType
import com.github.npospolita.worktimesheets.domain.WorkTimesheet
import com.github.npospolita.worktimesheets.domain.WorkTimesheetId
import com.github.npospolita.worktimesheets.domain.errors.ValidationError
import org.slf4j.LoggerFactory.getLogger
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class CheckInService(
    private val workTimesheetRepository: WorkTimesheetRepository
) {
    companion object {
        val log = getLogger(CheckInService::class.java.name)
    }

    private val zoneId = ZoneId.of("Europe/Moscow")

    fun checkIn(checkInType: CheckInType, employeeId: Long) {
        log.info("Received check_in: {} for employee: {}", checkInType, employeeId)

        val workTimesheet = workTimesheetRepository.findById(WorkTimesheetId(LocalDate.now(zoneId), employeeId))
            .orElse(createEmptyTimesheet(employeeId))

        fillCheckInFields(checkInType, workTimesheet)


        log.info("Saved work-timesheet: {}", workTimesheetRepository.save(workTimesheet))
    }

    private fun createEmptyTimesheet(employeeId: Long) = WorkTimesheet(
        WorkTimesheetId(LocalDate.now(zoneId), employeeId)
    )

    private fun fillCheckInFields(
        checkInType: CheckInType,
        workTimesheet: WorkTimesheet
    ) {
        when (checkInType) {
            CheckInType.IN -> {
                if (workTimesheet.startTime != null)
                    throw ValidationError("Вы уже отмечали начало работы сегодня " + LocalDate.now(zoneId))

                workTimesheet.startTime = getCurrentTime()
            }
            CheckInType.OUT -> {
                if (workTimesheet.startTime == null)
                    throw ValidationError("Вы ещё не отмечали начало работы сегодня " + LocalDate.now(zoneId))
                if (workTimesheet.endTime != null)
                    throw ValidationError("Вы уже отмечали конец работы сегодня " + LocalDate.now(zoneId))

                workTimesheet.endTime = getCurrentTime()
            }
        }
    }

    private fun getCurrentTime() = LocalDateTime.now(zoneId)


}