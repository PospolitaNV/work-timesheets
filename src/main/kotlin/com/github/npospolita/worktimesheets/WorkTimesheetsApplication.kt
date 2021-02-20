package com.github.npospolita.worktimesheets

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.boot.SpringApplication
import org.springframework.core.env.EnumerablePropertySource

import org.springframework.boot.context.event.ApplicationPreparedEvent

import org.springframework.core.env.ConfigurableEnvironment

import org.springframework.context.ApplicationListener
import java.util.*


@SpringBootApplication
@EnableJpaRepositories
class WorkTimesheetsApplication

fun main(args: Array<String>) {
    val springApplication = SpringApplication(WorkTimesheetsApplication::class.java)
    springApplication.addListeners(PropertiesLogger())
    runApplication<WorkTimesheetsApplication>(*args)
}

class PropertiesLogger : ApplicationListener<ApplicationPreparedEvent> {
    private var environment: ConfigurableEnvironment? = null
    private var isFirstRun = true
    override fun onApplicationEvent(event: ApplicationPreparedEvent) {
        if (isFirstRun) {
            environment = event.applicationContext.environment
            printProperties()
        }
        isFirstRun = false
    }

    fun printProperties() {
        for (propertySource in findPropertiesPropertySources()) {
            log.info("******* " + propertySource.name + " *******")
            val propertyNames = propertySource.propertyNames
            Arrays.sort(propertyNames)
            for (propertyName in propertyNames) {
                val resolvedProperty = environment!!.getProperty(propertyName!!)
                val sourceProperty = propertySource.getProperty(propertyName).toString()
                if (resolvedProperty == sourceProperty) {
                    log.info("{}={}", propertyName, resolvedProperty)
                } else {
                    log.info("{}={} OVERRIDDEN to {}", propertyName, sourceProperty, resolvedProperty)
                }
            }
        }
    }

    private fun findPropertiesPropertySources(): List<EnumerablePropertySource<*>> {
        val propertiesPropertySources: MutableList<EnumerablePropertySource<*>> = LinkedList()
        for (propertySource in environment!!.propertySources) {
            if (propertySource is EnumerablePropertySource<*>) {
                propertiesPropertySources.add(propertySource)
            }
        }
        return propertiesPropertySources
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(PropertiesLogger::class.java)
    }
}
