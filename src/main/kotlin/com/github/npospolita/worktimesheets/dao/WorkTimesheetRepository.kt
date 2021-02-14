package com.github.npospolita.worktimesheets.dao

import com.github.npospolita.worktimesheets.domain.WorkTimesheet
import com.github.npospolita.worktimesheets.domain.WorkTimesheetId
import org.springframework.data.repository.CrudRepository

interface WorkTimesheetRepository : CrudRepository<WorkTimesheet, WorkTimesheetId> {

    fun findAllByTakenIntoAccountFalseAndId_EmployeeId(id: Long): List<WorkTimesheet>

}