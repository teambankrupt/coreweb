package com.example.coreweb.scheduling

import com.example.coreweb.routing.Route
import com.example.coreweb.scheduling.jobs.PaymentReminderJob
import com.example.coreweb.scheduling.service.Schedule
import com.example.coreweb.scheduling.service.SchedulerService
import io.swagger.annotations.Api
import org.quartz.JobBuilder
import org.quartz.Scheduler
import org.quartz.TriggerBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@RestController
@Api(tags = ["Scheduler"])
class SchedulerController(
    private val scheduler: Scheduler,
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
                Instant.now().plus(15, ChronoUnit.SECONDS), false
            ),
            phone = phone,
            email = email,
            subject = subject,
            message = message
        )
        return "Reminder scheduled successfully and will be executed"
    }


}