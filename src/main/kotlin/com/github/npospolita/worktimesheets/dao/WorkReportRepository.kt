package com.github.npospolita.worktimesheets.dao

import org.springframework.data.repository.CrudRepository
import java.util.*

interface WorkReportRepository : CrudRepository<WorkReportRepository, UUID> {
}