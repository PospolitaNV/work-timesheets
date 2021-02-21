package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.entities.Update

fun getChatId(update: Update): Long {
    update.callbackQuery ?: return update.callbackQuery?.message?.chat?.id!!
    update.message ?: return update.message?.chat?.id!!
    throw Error("Can't extract chat id")
}

fun getUserId(update: Update): Long {
    update.callbackQuery ?: return update.callbackQuery?.message?.from?.id!!
    update.message ?: return update.message?.from?.id!!
    throw Error("Can't extract user id")
}