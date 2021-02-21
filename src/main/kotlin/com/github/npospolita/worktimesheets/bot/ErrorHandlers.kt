package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Update

class ErrorHandlers {
    companion object {
        fun unknownUser(): (bot: Bot, update: Update) -> Unit {
            return { bot, update ->
                bot.sendMessage(update.message?.chat?.id!!, "Вы не зарегистрированы в системе ):")
            }
        }

        fun accessDenied(): (bot: Bot, update: Update) -> Unit {
            return { bot, update ->
                bot.sendMessage(update.message?.chat?.id!!, "У вас нет сюда доступа :P")
            }
        }
    }
}