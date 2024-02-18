package com.example.coreweb.scheduling.service

import com.example.coreweb.scheduling.jobs.PaymentReminderJob
import com.example.coreweb.scheduling.jobs.ReminderJob
import org.quartz.JobBuilder
import org.quartz.Scheduler
import org.quartz.SimpleScheduleBuilder
import org.quartz.TriggerBuilder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

sealed interface ContactId {
    data class Phone(val phone: String) : ContactId
    data class Email(val email: String) : ContactId
}

data class Schedule(
    val time: Instant,
    val repeat: Boolean = false,
    val repeatInterval: Long = 0,
    val repeatCount: Int = 0
)

interface SchedulerService {
    fun scheduleReminder(
        schedule: Schedule, phone: String?, email: String?,
        subject: String, message: String
    )
}

@Service
class SchedulerServiceImpl(
    private val scheduler: Scheduler
) : SchedulerService {

    override fun scheduleReminder(
        schedule: Schedule,
        phone: String?, email: String?,
        subject: String, message: String
    ) {
        val jobDetail = JobBuilder.newJob(ReminderJob::class.java)
            .withIdentity("reminder-job", "generic-reminder-group")
            .usingJobData("subject", subject)
            .usingJobData("message", message)
            .usingJobData("phone", phone)
            .usingJobData("email", email)

        val trigger = TriggerBuilder.newTrigger()
            .withIdentity("reminder-trigger", "generic-reminder-group")
            .startAt(Date.from(schedule.time))

        if (schedule.repeat) {
            trigger.withSchedule(
                SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInMilliseconds(schedule.repeatInterval)
                    .withRepeatCount(schedule.repeatCount)
            )
        }

        scheduler.scheduleJob(jobDetail.build(), trigger.build())

    }
}
