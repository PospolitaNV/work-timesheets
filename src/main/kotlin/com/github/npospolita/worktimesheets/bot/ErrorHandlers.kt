package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.dispatcher.handlers.HandleUpdate

class ErrorHandlers {
    companion object {
        fun unknownUser(): HandleUpdate {
            return { bot, update ->
                bot.sendMessage(
                    getChatId(update),
                    "Вы не зарегистрированы в системе ):"
                )
            }
        }

        fun accessDenied(): HandleUpdate {
            return { bot, update ->
                bot.sendMessage(
                    getChatId(update),
                    "У вас нет сюда доступа :P"
                )
            }
        }
    }
}