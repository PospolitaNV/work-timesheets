package com.github.npospolita.worktimesheets.service

import com.github.npospolita.worktimesheets.dao.EmployeeRepository
import com.github.npospolita.worktimesheets.domain.Employee
import com.github.npospolita.worktimesheets.domain.errors.ValidationError
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class EmployeeService(
    private val employeeRepository: EmployeeRepository
) {

    fun addEmployee(employee: Employee) {
        employeeRepository.save(employee)
    }

    fun changeWage(employeeId: Long, wage: Int) {
        val employee = employeeRepository.findByIdOrNull(employeeId)
        employee ?: throw ValidationError("Такого сотрудника не существует")
        employee.wage = wage
        employeeRepository.save(employee)
    }

    fun removeEmployee(employeeId: Long) {
        employeeRepository.deleteById(employeeId)
    }

    fun getEmployees(): String {
        val stringBuilder = StringBuilder()
        for (employee in employeeRepository.findAll()) {
            stringBuilder.append("Имя: ${employee.firstName} ${employee.lastName}\n")
                .append("Зарплата: ${employee.wage}руб/час\n\n")
        }
        return stringBuilder.toString()
    }

    fun getEmployee(userId: Long): Employee {
        return employeeRepository.findById(userId).orElseThrow()
    }

}