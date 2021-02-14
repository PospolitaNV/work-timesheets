package com.github.npospolita.worktimesheets.domain

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Employee(
    @Id val id: Long,
    var firstName: String,
    var lastName: String,
    var wage: Int
)