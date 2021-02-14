package handlers

import com.github.npospolita.worktimesheets.service.SecurityService
import org.botlaxy.telegramit.spring.handler.dsl.SpringStepTelegramHandlerBuilder

public fun SpringStepTelegramHandlerBuilder.allowOnlyAdmin(context: SpringStepTelegramHandlerBuilder, userId: Long) {
    if(!context.getBean<SecurityService>().isAdmin(userId)) throw SecurityException()
}

public fun SpringStepTelegramHandlerBuilder.allowOnlyKnownUser(context: SpringStepTelegramHandlerBuilder, userId: Long) {
    if(!context.getBean<SecurityService>().isKnownUser(userId)) throw SecurityException()
}