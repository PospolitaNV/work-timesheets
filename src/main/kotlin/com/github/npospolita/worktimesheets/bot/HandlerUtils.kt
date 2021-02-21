package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.entities.Update

fun getChatId(update: Update): Long {
    update.callbackQuery ?: return update.callbackQuery?.message?.chat?.id!!
    update.message ?: return update.message?.chat?.id!!
    throw Error("Can't extract chat id")
}