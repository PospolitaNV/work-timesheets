package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.entities.*
import org.junit.Assert
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class HandlerUtilsKtTest {

    val from = User(100L, false, "test")

    @Test
    fun getChatId() {
        Assert.assertEquals(2L, getChatId(Update(1L, Message(1L, chat = Chat(2L, type = "test"), date = 123))))
        Assert.assertEquals(3L, getChatId(Update(1L, Message(1L, chat = Chat(3L, type = "test"), date = 123))))
        Assert.assertEquals(
            2L,
            getChatId(
                Update(
                    1L,
                    callbackQuery = CallbackQuery(
                        "test",
                        from,
                        Message(4L, chat = Chat(2L, type = "test"), date = 123),
                        data = "test",
                        chatInstance = "test"
                    )
                )
            )
        )
        Assert.assertEquals(
            3L,
            getChatId(
                Update(
                    1L,
                    callbackQuery = CallbackQuery(
                        "test",
                        from,
                        Message(4L, chat = Chat(3L, type = "test"), date = 123),
                        data = "test",
                        chatInstance = "test"
                    )
                )
            )
        )
    }

    @Test
    fun getUserId() {
        Assert.assertEquals(
            100L,
            getUserId(Update(1L, Message(1L, chat = Chat(2L, type = "test"), from = from, date = 123)))
        )
        Assert.assertEquals(
            100L,
            getUserId(
                Update(
                    1L,
                    callbackQuery = CallbackQuery(
                        "test",
                        from,
                        Message(4L, chat = Chat(2L, type = "test"), date = 123),
                        data = "test",
                        chatInstance = "test"
                    )
                )
            )
        )
        Assert.assertEquals(
            100L,
            getUserId(
                Update(
                    1L,
                    callbackQuery = CallbackQuery(
                        "test",
                        from,
                        Message(4L, chat = Chat(3L, type = "test"), date = 123),
                        data = "test",
                        chatInstance = "test"
                    )
                )
            )
        )

        Assert.assertThrows(Error::class.java) {
            getUserId(Update(1L, Message(1L, chat = Chat(3L, type = "test"), date = 123)))
        }
    }
}