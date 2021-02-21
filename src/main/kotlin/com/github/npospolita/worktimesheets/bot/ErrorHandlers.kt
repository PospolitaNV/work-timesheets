package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.dispatcher.handlers.HandleUpdate

class ErrorHandlers {
    companion object {
        fun unknownUser(): HandleUpdate {
            return { bot, update ->
                bot.sendMessage(
                    update.message?.chat?.id ?: update.callbackQuery?.message?.chat?.id!!,
                    "Вы не зарегистрированы в системе ):"
                )
            }
        }

        fun accessDenied(): HandleUpdate {
            return { bot, update ->
                bot.sendMessage(
                    update.message?.chat?.id ?: update.callbackQuery?.message?.chat?.id!!,
                    "У вас нет сюда доступа :P"
                )
            }
        }
    }
}