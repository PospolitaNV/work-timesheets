package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.dispatcher.handlers.HandleUpdate
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton

object StartHandlers {

    fun admin(): HandleUpdate {
        return { bot, update ->
            bot.sendMessage(
                getChatId(update), "Выберите операцию",
                replyMarkup = adminStartKeyboardMarkup()
            )
        }
    }

    fun employee(): HandleUpdate {
        return { bot, update ->
            bot.sendMessage(
                getChatId(update), "Выберите операцию",
                replyMarkup = employeeStartKeyboardMarkup()
            )
        }
    }

    fun employeeStartKeyboardMarkup() = InlineKeyboardMarkup.create(
        listOf(
            InlineKeyboardButton.CallbackData(
                text = "Отметить приход",
                callbackData = "check_in"
            ),
            InlineKeyboardButton.CallbackData(
                text = "Отметить уход",
                callbackData = "check_out"
            )
        ),
        listOf(
            InlineKeyboardButton.CallbackData(
                text = "Посмотреть статистику за неделю",
                callbackData = "employee_weekly_stats"
            )
        ),
        listOf(
            InlineKeyboardButton.CallbackData(
                text = "Посмотреть статистику по месяцам",
                callbackData = "employee_monthly_stats"
            )
        )
    )

    fun adminStartKeyboardMarkup() = InlineKeyboardMarkup.create(
        listOf(
            InlineKeyboardButton.CallbackData(
                text = "Сотрудники",
                callbackData = "employee_list"
            ),
            InlineKeyboardButton.CallbackData(
                text = "Расчёт зарплаты",
                callbackData = "salary_options"
            )
        )
    )
}