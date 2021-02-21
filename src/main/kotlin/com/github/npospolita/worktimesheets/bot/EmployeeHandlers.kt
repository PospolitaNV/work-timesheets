package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.dispatcher.handlers.HandleUpdate
import com.github.npospolita.worktimesheets.bot.StartHandlers.Companion.adminStartKeyboardMarkup
import com.github.npospolita.worktimesheets.bot.StartHandlers.Companion.employeeStartKeyboardMarkup
import com.github.npospolita.worktimesheets.service.WorkTimesheetService

class EmployeeHandlers {
    companion object {
        fun employeeStats(workTimesheetService: WorkTimesheetService): HandleUpdate {
            return { bot, update ->
                val text = workTimesheetService.checkTimesheets(getUserId(update))
                bot.sendMessage(
                    getChatId(update),
                    if (text.isBlank()) "График пустой!" else text,
                    replyMarkup = employeeStartKeyboardMarkup()
                )
            }
        }

        fun adminEmployeeStats(workTimesheetService: WorkTimesheetService): HandleUpdate {
            return { bot, update ->
                val text = workTimesheetService.checkTimesheets()
                bot.sendMessage(
                    getChatId(update),
                    if (text.isBlank()) "Графики пустые!" else text,
                    replyMarkup = adminStartKeyboardMarkup()
                )
            }
        }
    }
}