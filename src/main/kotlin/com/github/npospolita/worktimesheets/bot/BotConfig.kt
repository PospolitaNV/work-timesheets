package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
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

            command("hello") {
                    bot.sendMessage(message.chat.id, "Hey bruh!")
                }
                command("testKeyboard") {
                    bot.sendMessage(
                        message.chat.id, "testKeyboard",
                        replyMarkup = KeyboardReplyMarkup(
                            KeyboardButton("One"), KeyboardButton("Two")
                        )
                    )
                }
//                callbackQuery("callback1") {
//                    log.info("test1")
//                    log.info("$update")
//                }
//                callbackQuery("callback1") {
//                    log.info("test2")
//                    log.info("$update")
//                }
            }

        }

        bot.startWebhook()

        return bot
    }

}