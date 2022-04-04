package com.github.npospolita.worktimesheets.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/timesheet")
class TimesheetController {


    @GetMapping
    fun main(model: Model): String? {
        return "timesheet/main"
    }
}
