package com.github.npospolita.worktimesheets.controller

import com.github.npospolita.worktimesheets.dao.EmployeeRepository
import com.github.npospolita.worktimesheets.domain.Employee
import com.github.npospolita.worktimesheets.domain.dto.EmployeeCreateForm
import com.github.npospolita.worktimesheets.domain.dto.EmployeeEditDto
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@Controller
@RequestMapping("/employee")
class EmployeeController(
    val employeeRepository: EmployeeRepository
) {

    @GetMapping
    fun main(model: Model): String? {
        populateEmployees(model)
        return "employee/main"
    }

    @GetMapping("/edit")
    fun editPage(@RequestParam edit: Long, model: Model): String? {
        val dtos = populateEmployees(model)
        val employeeEditDto = dtos.find { it.id == edit }
        employeeEditDto?.isEdited = true
        model.addAttribute("employeeEditDto", employeeEditDto)
        return "employee/main"
    }

    @PostMapping("/edit")
    fun edit(@Valid @ModelAttribute employeeEditDto: EmployeeEditDto, bindingResult: BindingResult, model: Model): String? {
        if (bindingResult.hasErrors()) {
            val dtos = populateEmployees(model)
            dtos.find { it.id == employeeEditDto.id }?.isEdited = true
            model.addAttribute("employeeEditDto", employeeEditDto)
            return "employee/main"
        }
        employeeRepository.saveAll(listOf(employeeEditDto).map { Employee(it.id!!, it.firstName!!, it.lastName!!, it.wage!!) })
        return "redirect:/employee"
    }

    @GetMapping("/add")
    fun save(model: Model): String? {
        model.addAttribute("employeeCreateForm", EmployeeCreateForm(null, null, null, null))
        return "employee/create"
    }

    @PostMapping("/add")
    fun create(@Valid @ModelAttribute employeeCreateForm: EmployeeCreateForm, bindingResult: BindingResult, model: Model): String? {
        if (bindingResult.hasErrors()) {
            model.addAttribute("employeeCreateForm", employeeCreateForm)
            return "employee/create"
        }

        employeeRepository.saveAll(listOf(employeeCreateForm).map { Employee(it.id!!, it.firstName!!, it.lastName!!, it.wage!!) })
        return "redirect:/employee"
    }

    @GetMapping("/delete")
    fun delete(@RequestParam deleteId: Long, model: Model): String? {
        employeeRepository.deleteById(deleteId)
        return "redirect:/employee"
    }

    private fun populateEmployees(model: Model): List<EmployeeEditDto> {
        val dtos = employeeRepository.findAll()
            .map { EmployeeEditDto(it.id, it.firstName, it.lastName, it.wage) }
            .toMutableList()
            .sortedBy { it.id }
        model.addAttribute("employees", dtos)
        return dtos
    }



}
