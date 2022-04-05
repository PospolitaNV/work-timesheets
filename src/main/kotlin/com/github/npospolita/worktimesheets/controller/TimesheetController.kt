package com.github.npospolita.worktimesheets.controller

import com.github.npospolita.worktimesheets.dao.WorkTimesheetRepository
import com.github.npospolita.worktimesheets.domain.NotFilledTime
import com.github.npospolita.worktimesheets.domain.WorkTimesheetId
import com.github.npospolita.worktimesheets.domain.dto.TimesheetCorrectDto
import com.github.npospolita.worktimesheets.service.DateTimeProviderService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.time.*
import javax.validation.Valid

@Controller
@RequestMapping("/timesheet")
class TimesheetController(
    val workTimesheetRepository: WorkTimesheetRepository,
    val dateTimeProviderService: DateTimeProviderService
) {

    @GetMapping
    fun main(model: Model): String? {
        model.addAttribute("notFilledTimeDto", convertTo(workTimesheetRepository.getNotFilledTime()))
        return "timesheet/main"
    }

    private fun convertTo(notFilledTime: List<NotFilledTime>): List<TimesheetCorrectDto> {
        return notFilledTime
            .sortedBy { it.workDay }
            .map {
            TimesheetCorrectDto(
                it.workDay,
                it.employeeId,
                it.firstName,
                it.lastName,
                it.startTime.atZone(ZoneId.of("Europe/Moscow")).toLocalDateTime(),
                null
            )
        }
    }

    @PostMapping("/correct")
    fun correct(@Valid @ModelAttribute timesheetCorrectDto: TimesheetCorrectDto, bindingResult: BindingResult, model: Model): String? {
        if (!bindingResult.hasErrors()) {
            val workTimesheet = workTimesheetRepository.findById(
                WorkTimesheetId(
                    timesheetCorrectDto.workDay,
                    timesheetCorrectDto.employeeId
                )
            ).orElseThrow()
            workTimesheet.endTime = dateTimeProviderService.createCorrectEndTime(timesheetCorrectDto.startTime, timesheetCorrectDto.endTime)
            workTimesheetRepository.save(workTimesheet)
        }
        return "redirect:/timesheet"
    }

}
