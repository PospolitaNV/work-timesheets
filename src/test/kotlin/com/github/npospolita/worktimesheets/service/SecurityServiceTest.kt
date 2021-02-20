package com.github.npospolita.worktimesheets.service

import com.github.npospolita.worktimesheets.DatabaseTestBase
import com.github.npospolita.worktimesheets.dao.EmployeeRepository
import com.github.npospolita.worktimesheets.domain.Employee
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional

@SpringBootTest
@Transactional
class SecurityServiceTest(
    @Autowired val employeeRepository: EmployeeRepository,
    @Autowired val securityService: SecurityService
) : DatabaseTestBase() {

    @Test
    fun isPresentUser() {
        employeeRepository.save(Employee(1L, "test", "keke", 100))

        assertTrue(securityService.isKnownUser(1L))
        assertFalse(securityService.isKnownUser(2L))
    }

    @Test
    fun isAdmin() {
        assertTrue(securityService.isAdmin(5L))
        assertFalse(securityService.isAdmin(2L))
    }
}
