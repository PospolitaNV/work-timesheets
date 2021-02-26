package com.github.npospolita.worktimesheets.service

import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@Service
class DateTimeProviderService {

    private val zoneId = ZoneId.of("Europe/Moscow")

    fun getWorktimesheetDate(): LocalDate = LocalDate.now(ZoneOffset.UTC)

    fun getWorktimesheetTime(): LocalDateTime = LocalDateTime.now(zoneId)

}