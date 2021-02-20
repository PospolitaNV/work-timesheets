package com.github.npospolita.worktimesheets

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.boot.SpringApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


@SpringBootApplication
@EnableJpaRepositories
class WorkTimesheetsApplication {
}

@Configuration
class DatabaseConfig {

    @Bean
    fun dataSource(appContext: ApplicationContext): DataSource {
        println()
        println("HEROKU_POSTGRESQL_YELLOW_URL:" + appContext.environment.getProperty("HEROKU_POSTGRESQL_YELLOW_URL"))
        println("JDBC_DATABASE_USERNAME:" + appContext.environment.getProperty("JDBC_DATABASE_USERNAME"))
        println("JDBC_DATABASE_PASSWORD:" + appContext.environment.getProperty("JDBC_DATABASE_PASSWORD"))
        println("SPRING_DATASOURCE_URL:" + appContext.environment.getProperty("SPRING_DATASOURCE_URL"))
        println()
        return HikariDataSource()
    }

}

fun main(args: Array<String>) {
    println("--------------------------------")
    println(System.getProperties())
    println("--------------------------------")
    runApplication<WorkTimesheetsApplication>(*args)
}

