package com.github.npospolita.worktimesheets.service

import com.github.npospolita.worktimesheets.dao.EmployeeRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SecurityService(
    val employeeRepository: EmployeeRepository
) {

    @Value("#{'\${bot.domain.adminId}'.split(',')}")
    val adminIdList: List<Long> = listOf(0L)

    fun isKnownUser(userId: Long) = employeeRepository.findById(userId).isPresent

    fun isAdmin(userId: Long) = userId in adminIdList
}