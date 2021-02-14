package com.github.npospolita.worktimesheets

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.function.Supplier

@SpringBootTest
@Testcontainers
class DatabaseTestBase {

    companion object {

        @Container
        val postgres: PostgreSQLContainer<*> = PostgreSQLContainer<Nothing>("postgres:13.1")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.jpa.hibernate.ddl-auto") { "create" }
        }

    }
}

