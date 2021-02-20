package com.github.npospolita.worktimesheets.service

import com.github.npospolita.worktimesheets.dao.EmployeeRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SecurityService(
    val employeeRepository: EmployeeRepository
) {

    @Value("\${bot.domain.adminId}")
    val adminId: Long = 0L

    fun isKnownUser(userId: Long) = employeeRepository.findById(userId).isPresent

    fun isAdmin(userId: Long) = adminId == userId

}