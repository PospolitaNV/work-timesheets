package handlers

import com.github.npospolita.worktimesheets.domain.CheckInType
import com.github.npospolita.worktimesheets.domain.errors.ValidationError
import com.github.npospolita.worktimesheets.service.CheckInService
import org.botlaxy.telegramit.core.request.TextMessage
import org.botlaxy.telegramit.spring.handler.dsl.springHandler

val successfulCheckInMessage = "Вы успешно отметились!"

springHandler("/checkout") {
    process { ctx, args ->
        val userId = ctx.message.user?.id ?: return@process TextMessage("Непредвиденная ошибка")
        allowOnlyKnownUser(this, userId)

        try {
            getBean<CheckInService>().checkIn(CheckInType.OUT, userId)
        } catch (ex: ValidationError) {
            return@process TextMessage(ex.message!!)
        }
        TextMessage(successfulCheckInMessage)
    }
}