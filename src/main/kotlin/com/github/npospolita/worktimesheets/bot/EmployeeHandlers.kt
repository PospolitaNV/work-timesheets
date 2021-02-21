package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.dispatcher.handlers.HandleUpdate
import com.github.npospolita.worktimesheets.service.EmployeeService
import com.github.npospolita.worktimesheets.service.WorkTimesheetService

class EmployeeHandlers {
    companion object {
        fun employeeStats(workTimesheetService: WorkTimesheetService): HandleUpdate {
            return { bot, update ->
                val text = workTimesheetService.checkTimesheet(getUserId(update))
                bot.sendMessage(
                    getChatId(update),
                    if (text.isBlank()) "График пустой!" else text,
                    replyMarkup = StartHandlers.employeeStartKeyboardMarkup()
                )
            }
        }

        fun adminEmployeeStats(workTimesheetService: WorkTimesheetService): HandleUpdate {
            return { bot, update ->
                val text = workTimesheetService.checkTimesheets()
                bot.sendMessage(
                    getChatId(update),
                    if (text.isBlank()) "Графики пустые!" else text,
                    replyMarkup = StartHandlers.adminStartKeyboardMarkup()
                )
            }
        }

        fun employeeList(employeeService: EmployeeService): HandleUpdate {
            return { bot, update ->
                val text = employeeService.getEmployees()
                bot.sendMessage(
                    getChatId(update),
                    if (text.isBlank()) "Пока что у вас нет сотрудников!" else text,
                    replyMarkup = StartHandlers.adminStartKeyboardMarkup()
                )
            }
        }
    }
}