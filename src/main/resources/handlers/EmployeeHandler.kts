package handlers

import com.github.npospolita.worktimesheets.domain.Employee
import com.github.npospolita.worktimesheets.domain.errors.ValidationError
import com.github.npospolita.worktimesheets.service.EmployeeService
import com.github.npospolita.worktimesheets.service.WorkReportService
import org.botlaxy.telegramit.core.client.model.TelegramMessage
import org.botlaxy.telegramit.core.request.TextMessage
import org.botlaxy.telegramit.core.request.keyboard
import org.botlaxy.telegramit.spring.handler.dsl.springHandler

val successfulCheckInMessage = "Вы успешно отметились!"

springHandler("/employee") {
    step<String>("action") {
        entry { ctx, _ ->
            val userId = ctx.message.user?.id ?: return@entry TextMessage("Непредвиденная ошибка")
            this@springHandler.allowOnlyAdmin(this@springHandler, userId)

            val rateKeyboard = keyboard {
                row {
                    button("Список сотрудников")
                    button("Посмотреть график сотрудника")
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
            if (employee.text.equals("Список сотрудников")) {
                val employees = getBean<EmployeeService>().getEmployees()
                employees.forEach { stringBuilder.append(it.toString()).append("\n") }
            } else {
                //todo перейти в новый step или в handler /employee_timesheet
                TextMessage("Not implemented yet.")
            }
        } catch (ex: ValidationError) {
            TextMessage(ex.message!!)
        }
        TextMessage(stringBuilder.toString())
    }
}


