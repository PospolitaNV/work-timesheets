package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.dispatcher.handlers.HandleUpdate
import com.github.npospolita.worktimesheets.service.WorkTimesheetService

class EmployeeHandlers {
    companion object {
        fun employeeStats(workTimesheetService: WorkTimesheetService): HandleUpdate {
            return { bot, update ->
                bot.sendMessage(
                    update.message?.chat?.id!!,
                    workTimesheetService.checkTimesheets(update.message?.from?.id!!)
                )
            }
        }

        fun adminEmployeeStats(workTimesheetService: WorkTimesheetService): HandleUpdate {
            return { bot, update ->
                bot.sendMessage(
                    update.message?.chat?.id!!,
                    workTimesheetService.checkTimesheets()
                )
            }
        }
    }
}