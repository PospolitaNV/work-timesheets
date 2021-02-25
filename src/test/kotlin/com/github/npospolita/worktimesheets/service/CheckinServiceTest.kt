package com.github.npospolita.worktimesheets.service

import com.github.npospolita.worktimesheets.DatabaseTestBase
import com.github.npospolita.worktimesheets.dao.WorkTimesheetRepository
import com.github.npospolita.worktimesheets.domain.CheckInType
import com.github.npospolita.worktimesheets.domain.WorkTimesheetId
import com.github.npospolita.worktimesheets.domain.errors.ValidationError
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDate
import java.time.ZoneId
import javax.transaction.Transactional

@SpringBootTest
@Transactional
class CheckInServiceTest(
    @Autowired val checkInService: CheckInService,
    @Autowired val workTimesheetRepository: WorkTimesheetRepository
) : DatabaseTestBase() {

    @Test
    fun new_CheckIn() {
        checkInService.checkIn(CheckInType.IN, 1)

        val timesheetRecord =
            workTimesheetRepository.findByIdOrNull(WorkTimesheetId(LocalDate.now(ZoneId.of("Europe/Moscow")), 1))
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
            workTimesheetRepository.findByIdOrNull(WorkTimesheetId(LocalDate.now(ZoneId.of("Europe/Moscow")), 1))
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
}