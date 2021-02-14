package com.github.npospolita.worktimesheets.dao

import com.github.npospolita.worktimesheets.domain.WorkReport
import org.springframework.data.repository.CrudRepository
import java.util.*

interface WorkReportRepository : CrudRepository<WorkReport, UUID> {

    fun findAllByEmployeeId(employeeId: Long): List<WorkReport>

}