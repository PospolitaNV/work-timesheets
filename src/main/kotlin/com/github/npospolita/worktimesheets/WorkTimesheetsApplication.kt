package com.github.npospolita.worktimesheets

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WorkTimesheetsApplication

fun main(args: Array<String>) {
    runApplication<WorkTimesheetsApplication>(*args)
}
