package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.handlers.HandleUpdate
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.Update
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton

class StartHandlers {
    companion object {

        fun admin(): HandleUpdate {
            return { bot, update ->
                bot.sendMessage(
                    update.message?.chat?.id!!, "Выберите операцию",
                    replyMarkup = InlineKeyboardMarkup.create(
                        listOf(
                            InlineKeyboardButton.CallbackData(
                                text = "Сотрудники",
                                callbackData = "employee_list"
                            ),
                            InlineKeyboardButton.CallbackData(
                                text = "Расчёт зарплаты",
                                callbackData = "/salary"
                            )
                        )
                    )
                )
            }
        }

        fun employee(): HandleUpdate {
            return { bot, update ->
                bot.sendMessage(
                    update.message?.chat?.id!!, "Выберите операцию",
                    replyMarkup = InlineKeyboardMarkup.create(
                        listOf(
                            InlineKeyboardButton.CallbackData(
                                text = "Отметить приход",
                                callbackData = "check-in"
                            ),
                            InlineKeyboardButton.CallbackData(
                                text = "Отметить уход",
                                callbackData = "check-out"
                            )
                        ),
                        listOf(
                            InlineKeyboardButton.CallbackData(
                                text = "Посмотреть статистику",
                                callbackData = "employee-stats"
                            ),
                        )
                    )
                )
            }
        }

    }
}