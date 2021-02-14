package handlers

import com.github.npospolita.worktimesheets.domain.errors.ValidationError
import com.github.npospolita.worktimesheets.service.SecurityService
import com.github.npospolita.worktimesheets.service.WorkReportService
import org.botlaxy.telegramit.core.request.TextMessage
import org.botlaxy.telegramit.spring.handler.dsl.springHandler

val successfulCheckinMessage = "Вы успешно отметились!"

springHandler("/reports") {

    process { ctx, args ->
        val userId = ctx.message.user?.id ?: return@process TextMessage("Непредвиденная ошибка")

        try {
            getBean<SecurityService>().isAdmin(userId)
            getBean<WorkReportService>().makeAllReports()
        } catch (ex: ValidationError) {
            TextMessage(ex.message!!)
        }
        TextMessage(successfulCheckinMessage)
    }
}

