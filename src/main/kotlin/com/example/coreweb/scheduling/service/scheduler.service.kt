package com.example.coreweb.scheduling.service

import com.example.coreweb.scheduling.jobs.PaymentReminderJob
import com.example.coreweb.scheduling.jobs.ReminderJob
import org.quartz.JobBuilder
import org.quartz.JobKey
import org.quartz.Scheduler
import org.quartz.SimpleScheduleBuilder
import org.quartz.TriggerBuilder
import org.quartz.TriggerKey
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

data class Schedule(
    val time: Instant,
    val repeat: Boolean = false,
    val repeatInterval: Long = 0,
    val repeatCount: Int = 0
)

interface SchedulerService {
    fun scheduleReminder(
        uid: String, schedule: Schedule,
        phone: String?, email: String?,
        subject: String, message: String
    )
}

@Service
class SchedulerServiceImpl(
    private val scheduler: Scheduler
) : SchedulerService {

    override fun scheduleReminder(
        uid: String, schedule: Schedule,
        phone: String?, email: String?,
        subject: String, message: String
    ) {
        val jobKey = JobKey("reminder-job-$uid", "generic-reminder-group")
        val triggerKey = TriggerKey("reminder-trigger-$uid", "generic-reminder-group")

        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey)
        }

        if (scheduler.checkExists(triggerKey)) {
            scheduler.unscheduleJob(triggerKey)
        }

        val jobDetail = JobBuilder.newJob(ReminderJob::class.java)
            .withIdentity(jobKey)
            .usingJobData("subject", subject)
            .usingJobData("message", message)
            .usingJobData("phone", phone)
            .usingJobData("email", email)

        val trigger = TriggerBuilder.newTrigger()
            .withIdentity(triggerKey)
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
