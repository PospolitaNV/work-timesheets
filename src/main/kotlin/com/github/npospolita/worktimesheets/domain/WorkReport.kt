package com.github.npospolita.worktimesheets.domain

import java.math.BigInteger
import java.time.LocalDate
import java.util.*
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class WorkReport(@Id val id: UUID,
                      val employeeId: Int,
                      @ElementCollection val days: List<LocalDate> = ArrayList(),
                      val amount: BigInteger)
