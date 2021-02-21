package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.Update
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.logging.LogLevel
import com.github.kotlintelegrambot.webhook
import com.github.npospolita.worktimesheets.service.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BotConfig {
    companion object {
        val log = LoggerFactory.getLogger(BotConfig::class.java.name)
    }

    @Value("\${bot.hosted.url}")
    var hostedUrl: String? = null

    @Value("\${bot.token}")
    var botToken: String? = null

    @Bean
    fun telegramBot(
        @Autowired checkinService: CheckInService,
        @Autowired employeeService: EmployeeService,
        @Autowired securityService: SecurityService,
        @Autowired workReportService: WorkReportService,
        @Autowired workTimesheetService: WorkTimesheetService
    ): Bot {
        val bot = bot {
            token = botToken!!
            timeout = 30
            logLevel = LogLevel.Network.Body

            webhook {
                url = "$hostedUrl/$botToken"
                allowedUpdates = listOf("message")
            }

            dispatch {

                command("start") {
                    doBasedOnAuth(securityService,
                        { bot, update ->
                            bot.sendMessage(
                                update.message?.chat?.id!!, "Выберите операцию",
                                replyMarkup = InlineKeyboardMarkup.create(
                                    listOf(
                                        InlineKeyboardButton.CallbackData(
                                            text = "Сотрудники",
                                            callbackData = "employees"
                                        ),
                                        InlineKeyboardButton.CallbackData(
                                            text = "Расчёт зарплаты",
                                            callbackData = "salary"
                                        )
                                    )
                                )
                            )
                        },
                        { bot, update ->
                            bot.sendMessage(
                                update.message?.chat?.id!!, "Выберите операцию",
                                replyMarkup = InlineKeyboardMarkup.create(
                                    listOf(
                                        InlineKeyboardButton.CallbackData(
                                            text = "Отметить приход",
                                            callbackData = "check-in"
                                        ),
                                        InlineKeyboardButton.CallbackData(
                                            text = "Отметить уход",
                                            callbackData = "check-out"
                                        )
                                    ),
                                    listOf(
                                        InlineKeyboardButton.CallbackData(
                                            text = "Посмотреть статистику",
                                            callbackData = "employee-stats"
                                        ),
                                    )
                                )
                            )
                        },
                        { bot, update ->
                            bot.sendMessage(update.message?.chat?.id!!, "Вы не зарегистрированы в системе ):")
                        }
                    )
                }
                command("testKeyboard") {
                    bot.sendMessage(
                        message.chat.id, "testKeyboard",
                        replyMarkup = InlineKeyboardMarkup.create(
                            listOf(
                                InlineKeyboardButton.CallbackData(
                                    text = "Test Inline Button",
                                    callbackData = "callback1"
                                )
                            ),
                            listOf(InlineKeyboardButton.CallbackData(text = "Show alert", callbackData = "showAlert"))
                        )
                    )
                }
                callbackQuery("callback1") {
                    log.info("test1")
                    log.info("$update")
                }
                callbackQuery("showAlert") {
                    log.info("test2")
                    log.info("$update")
                }
            }

        }

        bot.startWebhook()

        return bot
    }

}

private fun CommandHandlerEnvironment.doBasedOnAuth(
    securityService: SecurityService,
    adminHandler: (bot: Bot, update: Update) -> Unit,
    knownUserHandler: (bot: Bot, update: Update) -> Unit,
    unknownUserHandler: (bot: Bot, update: Update) -> Unit
) {
    val userId = update.message?.from?.id!!
    when {
        securityService.isAdmin(userId) -> {
            adminHandler.invoke(bot, update)
        }
        securityService.isKnownUser(userId) -> {
            knownUserHandler.invoke(bot, update)
        }
        else -> {
            unknownUserHandler.invoke(bot, update)
        }
    }
}