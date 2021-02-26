package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.dispatcher.handlers.HandleUpdate
import com.github.npospolita.worktimesheets.domain.CheckInType
import com.github.npospolita.worktimesheets.domain.errors.ValidationError
import com.github.npospolita.worktimesheets.service.CheckInService

object CheckInOutHandlers {
    fun checkIn(checkInService: CheckInService): HandleUpdate {
        return { bot, update ->
            var message = "Вы успешно отметили свой приход"
            try {
                checkInService.checkIn(
                    CheckInType.IN,
                    getChatId(update)
                )
                checkInService.notifyAdmin(CheckInType.IN, getUserId(update), bot)
            } catch (e: ValidationError) {
                message = e.message!!
            }
            bot.sendMessage(getChatId(update), message, replyMarkup = StartHandlers.employeeStartKeyboardMarkup())
        }
    }

    fun checkOut(checkInService: CheckInService): HandleUpdate {
        return { bot, update ->
            var message = "Вы успешно отметили свой уход"
            try {
                checkInService.checkIn(
                    CheckInType.OUT,
                    getChatId(update)
                )
                checkInService.notifyAdmin(CheckInType.OUT, getUserId(update), bot)
            } catch (e: ValidationError) {
                message = e.message!!
            }
            bot.sendMessage(getChatId(update), message, replyMarkup = StartHandlers.employeeStartKeyboardMarkup())
        }
    }
}