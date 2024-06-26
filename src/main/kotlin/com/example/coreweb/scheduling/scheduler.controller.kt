package com.example.coreweb.scheduling

import com.example.coreweb.routing.Route
import com.example.coreweb.scheduling.service.Schedule
import com.example.coreweb.scheduling.service.SchedulerService
import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.time.temporal.ChronoUnit

@RestController
@Api(tags = ["Scheduler"])
class SchedulerController(
    private val schedulerService: SchedulerService
) {

    @GetMapping(Route.V2.Scheduler.REMINDER_TEST)
    fun reminderTest(
        @RequestParam("phone") phone: String,
        @RequestParam("email") email: String,
        @RequestParam("subject") subject: String,
        @RequestParam("message") message: String
    ): String {
        this.schedulerService.scheduleReminder(
            uid = System.currentTimeMillis().toString(),
            schedule = Schedule(
                startAt = Instant.now().plus(15, ChronoUnit.SECONDS), repeat = false
            ),
            phone = phone,
            email = email,
            subject = subject,
            message = message
        )
        return "Reminder scheduled successfully and will be executed"
    }


}