package handlers

import com.github.npospolita.worktimesheets.domain.errors.ValidationError
import org.botlaxy.telegramit.core.client.model.TelegramMessage
import org.botlaxy.telegramit.core.request.TextMessage
import org.botlaxy.telegramit.core.request.keyboard
import org.botlaxy.telegramit.spring.handler.dsl.springHandler

val successfulCheckInMessage = "Вы успешно отметились!"

springHandler("/main") {
    step<String>("action") {
        entry { ctx, _ ->
            val userId = ctx.message.user?.id ?: return@entry TextMessage("Непредвиденная ошибка")
            this@springHandler.allowOnlyAdmin(this@springHandler, userId)

            val rateKeyboard = keyboard {
                row {
                    button("Сотрудники")
                    button("Репорты")
                }
            }
            TextMessage("Выберите действие", replyKeyboard = rateKeyboard)
        }
    }
    process { ctx, args ->
        val userId = ctx.message.user?.id ?: return@process TextMessage("Непредвиденная ошибка")
        allowOnlyAdmin(this, userId)

        val employee = ctx.answer["action"] as TelegramMessage
        val stringBuilder = StringBuilder()
        try {
            if (employee.text.equals("Сотрудники")) {
                //todo Перейти в handler /employee
                TextMessage("")
            } else if (employee.text.equals("Репорты")) {
                //todo Перейти в handler /reports
                TextMessage("")
            } else {
                TextMessage("Not implemented yet.")
            }
        } catch (ex: ValidationError) {
            TextMessage(ex.message!!)
        }
        TextMessage(stringBuilder.toString())
    }
}


