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

data class Action(
    val jobClass: Class<out Job>,
    val notifier: Notifier,
    val data: Map<String, Any?>
)

data class Notifier(
    val email: Boolean,
    val phone: Boolean,
    val push: Boolean
)

const val DATA_KEY_BY_PHONE = "by_phone"
const val DATA_KEY_BY_EMAIL = "by_email"
const val DATA_KEY_BY_PUSH = "by_push"
const val REMINDER_GROUP_NAME = "generic-reminder-group"

interface SchedulerService {
    fun scheduleReminder(
        uid: String, group: String = REMINDER_GROUP_NAME,
        schedule: Schedule,
        phone: String?, email: String?,
        subject: String, message: String
    )

    fun schedule(
        uid: String,
        group: String = REMINDER_GROUP_NAME,
        schedule: Schedule,
        action: Action
    )

    fun removeIfExist(uid: String, group: String = REMINDER_GROUP_NAME)
}

@Service
class SchedulerServiceImpl(
    private val scheduler: Scheduler
) : SchedulerService {

    override fun scheduleReminder(
        uid: String, group: String,
        schedule: Schedule,
        phone: String?, email: String?,
        subject: String, message: String
    ) = this.schedule(
        uid = uid,
        group = group,
        schedule = schedule,
        action = Action(
            jobClass = ReminderJob::class.java,
            notifier = Notifier(
                email = true,
                phone = true,
                push = true
            ),
            data = mapOf(
                "subject" to subject,
                "message" to message,
                "phone" to phone,
                "email" to email
            )
        )
    )


    override fun schedule(uid: String, group: String, schedule: Schedule, action: Action) {

        val (jobKey, triggerKey) = keys(uid, group)

        this.removeIfExist(uid, group)

        val data = JobDataMap()
        data[DATA_KEY_BY_PHONE] = action.notifier.phone
        data[DATA_KEY_BY_EMAIL] = action.notifier.email
        data[DATA_KEY_BY_PUSH] = action.notifier.push
        data.putAll(action.data)

        val jobBuilder = JobBuilder.newJob(action.jobClass)
            .withIdentity(jobKey)
            .usingJobData(data)

        val trigger = TriggerBuilder.newTrigger()
            .withIdentity(triggerKey)
            .startAt(Date.from(schedule.startAt))

        schedule.endAt?.let { trigger.endAt(Date.from(it)) }

        if (schedule.repeat) {
            var builder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMilliseconds(schedule.repeatInterval)
            builder = if (schedule.endAt != null) {
                builder.repeatForever()
            } else {
                builder.withRepeatCount(schedule.repeatCount)
            }
            trigger.withSchedule(builder)
        }

        scheduler.scheduleJob(jobBuilder.build(), trigger.build())
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
