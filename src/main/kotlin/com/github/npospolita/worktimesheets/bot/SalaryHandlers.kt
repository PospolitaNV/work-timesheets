package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.dispatcher.handlers.HandleUpdate
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.npospolita.worktimesheets.service.WorkReportService
import org.slf4j.LoggerFactory

class SalaryHandlers {
    companion object {
        val log = LoggerFactory.getLogger(SalaryHandlers::class.java.name)

        fun salaryCalculation(workReportService: WorkReportService): HandleUpdate {
            return { bot, update ->
                workReportService.makeAllReports()
                bot.sendMessage(
                    getChatId(update),
                    workReportService.makeAllReports()
                )
            }
        }

        fun salaryOptions(): HandleUpdate {
            return { bot, update ->
                bot.sendMessage(
                    getChatId(update), "Выберите операцию",
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