package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.entities.Update

fun getChatId(update: Update): Long {
    if (update.callbackQuery != null) return update.callbackQuery?.message?.chat?.id!!
    if (update.message != null) return update.message?.chat?.id!!
    throw Error("Can't extract chat id")
}

fun getUserId(update: Update): Long {
    if (update.callbackQuery != null) return update.callbackQuery?.from?.id!!
    if (update.message != null && update.message?.from != null) return update.message?.from?.id!!
    throw Error("Can't extract user id")
}