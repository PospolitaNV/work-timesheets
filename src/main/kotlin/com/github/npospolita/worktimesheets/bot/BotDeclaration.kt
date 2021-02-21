package com.github.npospolita.worktimesheets.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.handlers.CallbackQueryHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.HandleUpdate
import com.github.kotlintelegrambot.logging.LogLevel
import com.github.kotlintelegrambot.webhook
import com.github.npospolita.worktimesheets.service.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BotDeclaration {
    companion object {
        val log = LoggerFactory.getLogger(BotDeclaration::class.java.name)
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
                    log.info("start handler")
                    doBasedOnAuth(
                        securityService,
                        StartHandlers.admin(),
                        StartHandlers.employee(),
                        ErrorHandlers.unknownUser()
                    )
                }
                callbackQuery("salary") {
                    log.info("salary handler")
                    doBasedOnAuth(
                        securityService,
                        SalaryHandlers.salaryOptions(),
                        ErrorHandlers.accessDenied(),
                        ErrorHandlers.accessDenied()
                    )
                }
                callbackQuery("salaryСalculation") {
                    doBasedOnAuth(
                        securityService,
                        SalaryHandlers.salaryCalculation(workReportService),
                        ErrorHandlers.accessDenied(),
                        ErrorHandlers.accessDenied()
                    )
                }
                callbackQuery("employeeStats") {
                    doBasedOnAuth(
                        securityService,
                        EmployeeHandlers.adminEmployeeStats(workTimesheetService),
                        EmployeeHandlers.employeeStats(workTimesheetService),
                        ErrorHandlers.accessDenied()
                    )
                }
                callbackQuery("check-in") {
                    doBasedOnAuth(
                        securityService,
                        ErrorHandlers.accessDenied(),
                        CheckInOutHandlers.checkIn(checkinService),
                        ErrorHandlers.accessDenied()
                    )
                }
                callbackQuery("check-out") {
                    doBasedOnAuth(
                        securityService,
                        ErrorHandlers.accessDenied(),
                        CheckInOutHandlers.checkOut(checkinService),
                        ErrorHandlers.accessDenied()
                    )
                }
            }

        }

        bot.startWebhook()

        return bot
    }

}

private fun CommandHandlerEnvironment.doBasedOnAuth(
    securityService: SecurityService,
    adminHandler: HandleUpdate,
    knownUserHandler: HandleUpdate,
    unknownUserHandler: HandleUpdate
) {
    val userId = getUserId(update)
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

private fun CallbackQueryHandlerEnvironment.doBasedOnAuth(
    securityService: SecurityService,
    adminHandler: HandleUpdate,
    knownUserHandler: HandleUpdate,
    unknownUserHandler: HandleUpdate
) {
    val userId = getUserId(update)
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