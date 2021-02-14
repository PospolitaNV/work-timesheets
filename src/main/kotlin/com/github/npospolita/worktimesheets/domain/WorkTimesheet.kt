package com.github.npospolita.worktimesheets.domain

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity

@Embeddable
data class WorkTimesheetId(var day: LocalDate,
                      var employeeId: Int) : Serializable

@Entity
data class WorkTimesheet(@EmbeddedId val id: WorkTimesheetId,
                         var startTime: LocalDateTime?,
                         var endTime: LocalDateTime?,
                         val takenIntoAccount: Boolean)

