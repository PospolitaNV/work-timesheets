package com.github.npospolita.worktimesheets.service

import com.github.npospolita.worktimesheets.DatabaseTestBase
import com.github.npospolita.worktimesheets.dao.EmployeeRepository
import com.github.npospolita.worktimesheets.domain.Employee
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import javax.transaction.Transactional

@SpringBootTest
@Transactional
class EmployeeServiceTest(
    @Autowired val employeeService: EmployeeService,
    @Autowired val employeeRepository: EmployeeRepository
) : DatabaseTestBase() {


    @Test
    fun employeeCreated() {
        employeeService.addEmployee(Employee(1L, "testFirst", "testLast", 100))

        val employee = employeeRepository.findByIdOrNull(1L)
        assertEquals(1L, employee!!.id)
        assertEquals("testFirst", employee.firstName)
        assertEquals("testLast", employee.lastName)
        assertEquals(100, employee.wage)
    }

    @Test
    fun employeeDeleted() {
        employeeService.addEmployee(Employee(1L, "testFirst", "testLast", 100))
        employeeService.removeEmployee(1L)

        val employee = employeeRepository.findByIdOrNull(1L)
        assertNull(employee)
    }

    @Test
    fun employeeWageChanged() {
        employeeService.addEmployee(Employee(1L, "testFirst", "testLast", 100))
        employeeService.changeWage(1L,200)

        val employee = employeeRepository.findByIdOrNull(1L)
        assertNotNull(employee)
        assertEquals(employee!!.wage, 200)
    }

}