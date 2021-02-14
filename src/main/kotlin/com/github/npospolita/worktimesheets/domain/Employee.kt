package com.github.npospolita.worktimesheets.domain

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Employee(@Id val id: Int,
                    val firstName: String,
                    val lastName: String,
                    val wage: Int)