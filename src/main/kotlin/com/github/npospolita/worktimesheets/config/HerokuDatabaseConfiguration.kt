package com.github.npospolita.worktimesheets.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URI
import javax.sql.DataSource

@Configuration
class HerokuDatabaseConfiguration {

    @Bean
    fun dataSource(
        @Value("\${custom-datasource.url}") uri: String,
        @Value("\${custom-datasource.ssl-enabled}") sslEnabled: Boolean
    ): DataSource {
        val dbUri = URI(uri)

        val username: String = dbUri.userInfo.split(":")[0]
        val password: String = dbUri.userInfo.split(":")[1]
        val dbUrl = "jdbc:postgresql://" + dbUri.host + ':' + dbUri.port + dbUri.path
            .toString() + if (sslEnabled) "?sslmode=require" else ""
        val basicDataSource = HikariDataSource()
        basicDataSource.jdbcUrl = dbUrl
        basicDataSource.username = username
        basicDataSource.password = password
        return basicDataSource
    }

}