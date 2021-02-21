package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Update
import com.github.npospolita.worktimesheets.service.WorkTimesheetService

class EmployeeHandlers {
    companion object {
        fun employeeStats(workTimesheetService: WorkTimesheetService): (bot: Bot, update: Update) -> Unit {
            return { bot, update ->
                bot.sendMessage(
                    update.message?.chat?.id!!,
                    workTimesheetService.checkTimesheets(update.message?.from?.id!!)
                )
            }
        }

        fun adminEmployeeStats(workTimesheetService: WorkTimesheetService): (bot: Bot, update: Update) -> Unit {
            return { bot, update ->
                bot.sendMessage(
                    update.message?.chat?.id!!,
                    workTimesheetService.checkTimesheets()
                )
            }
        }
    }
}