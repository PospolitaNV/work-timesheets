package handlers

import com.github.npospolita.worktimesheets.domain.Employee
import com.github.npospolita.worktimesheets.domain.errors.ValidationError
import com.github.npospolita.worktimesheets.service.EmployeeService
import com.github.npospolita.worktimesheets.service.WorkReportService
import org.botlaxy.telegramit.core.client.model.TelegramMessage
import org.botlaxy.telegramit.core.client.model.TelegramRequest
import org.botlaxy.telegramit.core.request.TextMessage
import org.botlaxy.telegramit.core.request.keyboard
import org.botlaxy.telegramit.spring.handler.dsl.SpringStepTelegramHandlerBuilder
import org.botlaxy.telegramit.spring.handler.dsl.springHandler

val successfulCheckInMessage = "Вы успешно отметились!"

springHandler("/reports") {
    step<String>("employee") {
        entry { ctx, _ ->
            val userId = ctx.message.user?.id ?: return@entry TextMessage("Непредвиденная ошибка")
            this@springHandler.allowOnlyAdmin(this@springHandler, userId)

            val employees: MutableIterable<Employee> = this@springHandler.getBean<EmployeeService>().getEmployees()

            val rateKeyboard = keyboard {
                row {
                    employees.forEach { employee -> button(employee.firstName + " " + employee.lastName) }
                    button("Для всех")
                }
            }
            TextMessage("Для какого работника вы хотели рассчитать зарплату?", replyKeyboard = rateKeyboard)
        }
    }
    process { ctx, args ->
        val userId = ctx.message.user?.id ?: return@process TextMessage("Непредвиденная ошибка")
        allowOnlyAdmin(this, userId)

        val employee = ctx.answer["employee"] as TelegramMessage

        try {
            if (employee.text == "Для всех") getBean<WorkReportService>().makeAllReports()
            else {
                getBean<WorkReportService>().makeReport(employee.text!!)
            }
        } catch (ex: ValidationError) {
            TextMessage(ex.message!!)
        }
        TextMessage("End.")
    }
}

