package com.github.npospolita.worktimesheets.domain.dto

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


data class EmployeeEditDto(
    @field:NotNull var id: Long?,
    @field:NotBlank var firstName: String?,
    @field:NotBlank var lastName: String?,
    @field:NotNull @field:Min(50) var wage: Int?,
    var isEdited: Boolean = false
) {

}
