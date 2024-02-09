package com.github.npospolita.worktimesheets

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class DatabaseTestBase {

    companion object {

        @Container
        val postgres: PostgreSQLContainer<*> = PostgreSQLContainer<Nothing>("postgres:13.1")

        @JvmStatic
        @DynamicPropertySource
        fun infrastructureProperties(registry: DynamicPropertyRegistry) {
            registry.add("custom-datasource.url") {
                "postgres://${postgres.username}:${postgres.password}@${postgres.host}:${postgres.firstMappedPort}/${postgres.databaseName}"
            }
            registry.add("spring.jpa.hibernate.ddl-auto") { "create" }
            registry.add("custom-datasource.ssl-enabled") { "false" }
        }


        @JvmStatic
        @DynamicPropertySource
        fun businessProperties(registry: DynamicPropertyRegistry) {
            registry.add("bot.domain.adminId") { listOf<Long>(5L) }
        }
    }

}

