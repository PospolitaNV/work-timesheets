package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.dispatcher.handlers.HandleUpdate
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.npospolita.worktimesheets.service.WorkReportService

object SalaryHandlers {

    fun salaryCalculation(workReportService: WorkReportService): HandleUpdate {
        return { bot, update ->
            val text = workReportService.makeAllReports()
            bot.sendMessage(
                getChatId(update),
                if (text.isBlank()) "Отчёты пустые!" else text,
                replyMarkup = StartHandlers.adminStartKeyboardMarkup()
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
                            callbackData = "salary_calculation"
                        ),
                        InlineKeyboardButton.CallbackData(
                            text = "Посмотреть статистику работников",
                            callbackData = "employee_weekly_stats"
                        )
                    )
                )
            )
        }
    }
}