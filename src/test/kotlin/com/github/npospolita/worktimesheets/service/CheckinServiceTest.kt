package com.github.npospolita.worktimesheets.service

import com.github.npospolita.worktimesheets.DatabaseTestBase
import com.github.npospolita.worktimesheets.dao.WorkTimesheetRepository
import com.github.npospolita.worktimesheets.domain.CheckInType
import com.github.npospolita.worktimesheets.domain.WorkTimesheetId
import com.github.npospolita.worktimesheets.domain.errors.ValidationError
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate
import java.time.LocalDateTime
import javax.transaction.Transactional

@SpringBootTest
@ContextConfiguration(classes = [CheckInServiceTest.Config::class])
@Transactional
class CheckInServiceTest(
    @Autowired val checkInService: CheckInService,
    @Autowired val workTimesheetRepository: WorkTimesheetRepository,
    @Autowired val dateTimeProviderService: DateTimeProviderService
) : DatabaseTestBase() {

    @TestConfiguration
    class Config {
        @Bean
        @Primary
        fun dateTimeProviderServiceSpy(): DateTimeProviderService {
            return Mockito.spy(DateTimeProviderService())
        }
    }

    @Test
    fun new_CheckIn() {
        checkInService.checkIn(CheckInType.IN, 1)

        val timesheetRecord =
            workTimesheetRepository.findByIdOrNull(WorkTimesheetId(dateTimeProviderService.getWorktimesheetDate(), 1))
        assertNotNull(timesheetRecord)
        assertNotNull(timesheetRecord?.startTime)
    }

    @Test
    fun new_CheckOut() {
        assertThrows<ValidationError> {
            checkInService.checkIn(CheckInType.OUT, 1)
        }
    }

    @Test
    fun existed_CheckInTwice() {
        checkInService.checkIn(CheckInType.IN, 1)

        assertThrows<ValidationError> {
            checkInService.checkIn(CheckInType.IN, 1)
        }
    }

    @Test
    fun existed_CheckOut() {
        checkInService.checkIn(CheckInType.IN, 1)
        checkInService.checkIn(CheckInType.OUT, 1)

        val timesheetRecord =
            workTimesheetRepository.findByIdOrNull(WorkTimesheetId(dateTimeProviderService.getWorktimesheetDate(), 1))
        assertNotNull(timesheetRecord)
        assertNotNull(timesheetRecord!!.startTime)
        assertNotNull(timesheetRecord.endTime)
    }

    @Test
    fun existed_CheckOutTwice() {
        checkInService.checkIn(CheckInType.IN, 1)
        checkInService.checkIn(CheckInType.OUT, 1)

        assertThrows<ValidationError> {
            checkInService.checkIn(CheckInType.OUT, 1)
        }
    }

    @Test
    fun checkOutAfterMidnight() {
        `when`(dateTimeProviderService.getWorktimesheetDate()).thenReturn(LocalDate.of(2020, 12, 12))
        `when`(dateTimeProviderService.getWorktimesheetTime()).thenReturn(LocalDateTime.of(2020, 12, 12, 10, 0))
        checkInService.checkIn(CheckInType.IN, 1)
        `when`(dateTimeProviderService.getWorktimesheetTime()).thenReturn(LocalDateTime.of(2020, 12, 13, 0, 20))
        checkInService.checkIn(CheckInType.OUT, 1)

        val timesheetRecord =
            workTimesheetRepository.findByIdOrNull(WorkTimesheetId(dateTimeProviderService.getWorktimesheetDate(), 1))
        assertNotNull(timesheetRecord)
        assertNotNull(timesheetRecord!!.startTime)
        assertNotNull(timesheetRecord.endTime)
    }
}