package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.Update
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.npospolita.worktimesheets.service.WorkReportService

class SalaryHandlers {
    companion object {

        fun salaryCalculation(workReportService: WorkReportService): (bot: Bot, update: Update) -> Unit {
            return { bot, update ->
                workReportService.makeAllReports()
                bot.sendMessage(update.message?.chat?.id!!, workReportService.makeAllReports())
            }
        }

        fun salaryOptions(): (bot: Bot, update: Update) -> Unit {
            return { bot, update ->
                bot.sendMessage(
                    update.message?.chat?.id!!, "Выберите операцию",
                    replyMarkup = InlineKeyboardMarkup.create(
                        listOf(
                            InlineKeyboardButton.CallbackData(
                                text = "Посчитать зарплату",
                                callbackData = "salary-calculation"
                            ),
                            InlineKeyboardButton.CallbackData(
                                text = "Посмотреть статистику работников",
                                callbackData = "employee-stats"
                            )
                        )
                    )
                )
            }
        }

    }
}