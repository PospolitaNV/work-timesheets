package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Update
import com.github.npospolita.worktimesheets.domain.CheckInType
import com.github.npospolita.worktimesheets.domain.errors.ValidationError
import com.github.npospolita.worktimesheets.service.CheckInService

class CheckInOutHandlers {
    companion object {
        fun checkIn(checkInService: CheckInService): (bot: Bot, update: Update) -> Unit {
            return { bot, update ->
                var message = "Вы успешно отметили свой приход"
                try {
                    checkInService.checkIn(CheckInType.IN, update.message?.chat?.id!!)
                } catch (e: ValidationError) {
                    message = e.message!!
                }
                bot.sendMessage(update.message?.chat?.id!!, message)
            }
        }

        fun checkOut(checkInService: CheckInService): (bot: Bot, update: Update) -> Unit {
            return { bot, update ->
                var message = "Вы успешно отметили свой уход"
                try {
                    checkInService.checkIn(CheckInType.OUT, update.message?.chat?.id!!)
                } catch (e: ValidationError) {
                    message = e.message!!
                }
                bot.sendMessage(update.message?.chat?.id!!, message)
            }
        }
    }
}