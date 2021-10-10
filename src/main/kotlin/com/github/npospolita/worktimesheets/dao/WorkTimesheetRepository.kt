package com.github.npospolita.worktimesheets.dao

import com.github.npospolita.worktimesheets.domain.MonthlyReport
import com.github.npospolita.worktimesheets.domain.WorkTimesheet
import com.github.npospolita.worktimesheets.domain.WorkTimesheetId
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface WorkTimesheetRepository : CrudRepository<WorkTimesheet, WorkTimesheetId> {

    fun findAllByTakenIntoAccountFalseAndId_EmployeeId(id: Long): List<WorkTimesheet>

    @Query(
        value = """
        SELECT
            e.first_name as name,
            to_char(date_trunc('month', wt.day), 'MM Mon YYYY') AS month,
            SUM(ceil(extract(HOURS FROM (end_time - start_time)) + extract(MINUTES FROM (end_time - start_time))/60)) *
            (select wage from employee kek where kek.id = :userId) as salary
        FROM work_timesheet wt
                 join employee e on e.id = wt.employee_id
        where employee_id = :userId
        GROUP BY e.first_name, month
        ORDER BY month
    """,
        nativeQuery = true
    )
    fun monthlyReportByEmployeeId(@Param("userId") userId: Long): List<MonthlyReport>
}