package com.github.npospolita.worktimesheets

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
class WorkTimesheetsApplication

fun main(args: Array<String>) {
    runApplication<WorkTimesheetsApplication>(*args)
}
