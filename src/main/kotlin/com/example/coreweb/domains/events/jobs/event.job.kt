package com.example.coreweb.domains.events.jobs

import com.example.common.utils.Validator
import com.example.coreweb.domains.mail.services.MailService
import com.example.coreweb.domains.sms.enums.Providers
import com.example.coreweb.domains.sms.services.SmsService
import com.example.coreweb.scheduling.templates.reminderTemplate
import org.quartz.Job
import org.quartz.JobDataMap
import org.quartz.JobExecutionContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

data class PushNotifierEvent(
    val s: Any,
    val eventId: Long,
    val referenceId: Long?,
    val image: String?,
    val username: String,
    val subject: String,
    val message: String
) : ApplicationEvent(s)

@Component
class EventNotifierJob(
    private val smsService: SmsService,
    private val mailService: MailService,
    private val applicationEventPublisher: ApplicationEventPublisher
) : Job {

    private val logger: Logger = LoggerFactory.getLogger(EventNotifierJob::class.java)

    override fun execute(jobContext: JobExecutionContext?) {
        val data: JobDataMap = jobContext?.mergedJobDataMap ?: return
        val eventId = data.getLong("event_id")
        val subject = data.getString("subject")
        val message = data.getString("message")
        val image: String? = data.getString("image")
        val username = data.getString("username")
        val phone: String? = data.getString("phone")
        val email: String? = data.getString("email")
        val referenceId: Long = data.getLong("reference_id")

        this.applicationEventPublisher.publishEvent(
            PushNotifierEvent(
                s = this,
                eventId = eventId,
                username = username,
                image = image,
                subject = "Reminder: $subject",
                message = message.take(50),
                referenceId = referenceId
            )
        )

        phone?.let {
            if (Validator.isValidPhoneNumber("BD", it)) {
                logger.debug("Sending reminder to phone: $it")
                try {
                    smsService.sendSms(Providers.ADN_SMS, it, "$subject: $message")
                } catch (e: Exception) {
                    logger.error("Error sending reminder to phone: $it", e)
                }
                logger.debug("Reminder sent to phone: $it")
            }
        }

        email?.let {
            if (Validator.isValidEmail(it)) {
                logger.debug("Sending reminder to email: $it")
                mailService.send(
                    it, subject, reminderTemplate(subject, message), true
                )
                logger.debug("Reminder sent to email: $it")
            }
        }

    }

}