package com.github.npospolita.worktimesheets.service

import org.springframework.stereotype.Service
import java.time.*

@Service
class DateTimeProviderService {

    private val zoneId = ZoneId.of("Europe/Moscow")

    fun getWorktimesheetDate(): LocalDate = LocalDate.now(ZoneOffset.UTC)

    fun getWorktimesheetTime(): LocalDateTime = LocalDateTime.now(zoneId)

    fun createCorrectEndTime(startDateTime: LocalDateTime, endTime: String?): LocalDateTime {
        var endDateTime =
            ZonedDateTime.of(startDateTime.toLocalDate(), LocalTime.parse(endTime), ZoneId.of("Europe/Moscow"))
                .withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()
        while (endDateTime.isBefore(startDateTime)) {
            endDateTime = endDateTime.plusDays(1)
        }
        return endDateTime
    }

}
