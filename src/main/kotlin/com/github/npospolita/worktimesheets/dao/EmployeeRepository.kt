package com.github.npospolita.worktimesheets.dao

import com.github.npospolita.worktimesheets.domain.Employee
import org.springframework.data.repository.CrudRepository
import java.util.*

interface EmployeeRepository : CrudRepository<Employee, Long> {
    fun findByFirstNameAndLastName(firstName: String, lastName: String): Employee?
}