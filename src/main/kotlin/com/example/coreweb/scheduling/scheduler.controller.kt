package com.example.coreweb.scheduling

import com.example.coreweb.routing.Route
import com.example.coreweb.scheduling.jobs.PaymentReminderJob
import io.swagger.annotations.Api
import org.quartz.JobBuilder
import org.quartz.Scheduler
import org.quartz.TriggerBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.*

@RestController
@Api(tags = ["Scheduler"])
class SchedulerController(
    private val scheduler: Scheduler
) {

    @GetMapping(Route.V2.Scheduler.SMS_SCHEDULER_TEST)
    fun smsSchedulerTest(
        @RequestParam("phone") phone: String,
        @RequestParam("message") message: String
    ): String {
        val jobDetail = JobBuilder.newJob(PaymentReminderJob::class.java)
            .withIdentity("payment-reminder-job", "payment-reminder-group")
            .usingJobData("phone", phone)
            .usingJobData("message", message)
            .build()

        val scheduledTime = Instant.now().plus(1, java.time.temporal.ChronoUnit.MINUTES)
        val trigger = TriggerBuilder.newTrigger()
            .withIdentity("payment-reminder-trigger", "payment-reminder-group")
            .startAt(
                Date.from(scheduledTime)
            )
            .build()
        scheduler.scheduleJob(jobDetail, trigger)
        return "Job scheduled successfully and will be executed at $scheduledTime"
    }

}