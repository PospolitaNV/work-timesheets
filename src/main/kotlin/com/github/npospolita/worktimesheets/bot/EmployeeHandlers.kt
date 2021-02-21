package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.dispatcher.handlers.HandleUpdate
import com.github.kotlintelegrambot.entities.Update
import com.github.npospolita.worktimesheets.service.WorkTimesheetService

class EmployeeHandlers {
    companion object {
        fun employeeStats(workTimesheetService: WorkTimesheetService): HandleUpdate {
            return { bot, update ->
                bot.sendMessage(
                    getChatId(update),
                    workTimesheetService.checkTimesheets(getUserId(update))
                )
            }
        }

        fun adminEmployeeStats(workTimesheetService: WorkTimesheetService): HandleUpdate {
            return { bot, update ->
                bot.sendMessage(
                    getChatId(update),
                    workTimesheetService.checkTimesheets()
                )
            }
        }
    }
}