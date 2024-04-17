package com.example.coreweb.scheduling.service

import com.example.coreweb.scheduling.jobs.ReminderJob
import org.quartz.*
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

data class Schedule(
    val startAt: Instant,
    val endAt: Instant? = null,
    val repeat: Boolean = false,
    val repeatInterval: Long = 0,
    val repeatCount: Int = 0
)

const val REMINDER_GROUP_NAME = "generic-reminder-group"

interface SchedulerService {
    fun scheduleReminder(
        uid: String, schedule: Schedule,
        phone: String?, email: String?,
        subject: String, message: String
    )

    fun removeIfExist(uid: String, group: String)
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

        val (jobKey, triggerKey) = keys(uid, REMINDER_GROUP_NAME)

        this.removeIfExist(uid, REMINDER_GROUP_NAME)

        val jobDetail = JobBuilder.newJob(ReminderJob::class.java)
            .withIdentity(jobKey)
            .usingJobData("subject", subject)
            .usingJobData("message", message)
            .usingJobData("phone", phone)
            .usingJobData("email", email)

        val trigger = TriggerBuilder.newTrigger()
            .withIdentity(triggerKey)
            .startAt(Date.from(schedule.startAt))

        schedule.endAt?.let { trigger.endAt(Date.from(it)) }

        if (schedule.repeat) {
            var builder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMilliseconds(schedule.repeatInterval)
            if (schedule.endAt != null) {
                builder = builder.repeatForever()
            } else {
                builder = builder.withRepeatCount(schedule.repeatCount)
            }
            trigger.withSchedule(builder)
        }

        scheduler.scheduleJob(jobDetail.build(), trigger.build())

    }

    override fun removeIfExist(uid: String, group: String) {
        val (jobKey, triggerKey) = keys(uid, group)

        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey)
        }

        if (scheduler.checkExists(triggerKey)) {
            scheduler.unscheduleJob(triggerKey)
        }
    }

    fun keys(uid: String, group: String): Pair<JobKey, TriggerKey> =
        Pair(JobKey("job-$uid", group), TriggerKey("trigger-$uid", group))
}
