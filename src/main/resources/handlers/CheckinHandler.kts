package handlers

import com.github.npospolita.worktimesheets.domain.CheckInType
import com.github.npospolita.worktimesheets.domain.errors.ValidationError
import com.github.npospolita.worktimesheets.service.CheckInService
import org.botlaxy.telegramit.core.request.TextMessage
import org.botlaxy.telegramit.spring.handler.dsl.springHandler

val successfulCheckinMessage = "Вы успешно отметились!"

springHandler("/checkin") {

    process { ctx, args ->
        val userId = ctx.message.user?.id ?: return@process TextMessage("Непредвиденная ошибка")

        try {
            getBean<CheckInService>().checkIn(CheckInType.IN, userId)
        } catch (ex: ValidationError) {
            TextMessage(ex.message!!)
        }
        TextMessage(successfulCheckinMessage)
    }
}

springHandler("/checkout") {
    process { ctx, args ->
        val userId = ctx.message.user?.id ?: return@process TextMessage("Непредвиденная ошибка")

        try {
            getBean<CheckInService>().checkIn(CheckInType.OUT, userId)
        } catch (ex: ValidationError) {
            TextMessage(ex.message!!)
        }
        TextMessage(successfulCheckinMessage)
    }
}
