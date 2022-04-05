package com.github.npospolita.worktimesheets.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    val passwordEncoder: PasswordEncoder
) : WebSecurityConfigurerAdapter() {

    @Value("\${admin.pass}")
    lateinit var adminPass: String

    @Value("\${admin.login}")
    lateinit var adminLogin: String

    @Value("\${bot.token}")
    lateinit var botToken: String

    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
                .antMatchers("/$botToken").permitAll()
                .antMatchers("/styles.css").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
            .loginProcessingUrl("/login")
                .permitAll()
                .successForwardUrl("/employee")
                .and()
            .logout()
                .permitAll()
    }

    @Bean
    override fun userDetailsService(): UserDetailsService {
        val user = User.builder()
            .username(adminLogin)
            .password(passwordEncoder.encode(adminPass))
            .roles("USER")
            .build()

        return InMemoryUserDetailsManager(user)
    }
}
