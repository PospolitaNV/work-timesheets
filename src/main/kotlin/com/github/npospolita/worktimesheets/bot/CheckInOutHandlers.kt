package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.handlers.HandleUpdate
import com.github.kotlintelegrambot.entities.Update
import com.github.npospolita.worktimesheets.domain.CheckInType
import com.github.npospolita.worktimesheets.domain.errors.ValidationError
import com.github.npospolita.worktimesheets.service.CheckInService

class CheckInOutHandlers {
    companion object {
        fun checkIn(checkInService: CheckInService): HandleUpdate {
            return { bot, update ->
                var message = "Вы успешно отметили свой приход"
                try {
                    checkInService.checkIn(
                        CheckInType.IN,
                        getChatId(update)
                    )
                } catch (e: ValidationError) {
                    message = e.message!!
                }
                bot.sendMessage(getChatId(update), message)
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
                } catch (e: ValidationError) {
                    message = e.message!!
                }
                bot.sendMessage(getChatId(update), message)
            }
        }
    }
}