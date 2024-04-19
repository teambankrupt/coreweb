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
        val username = data.getString("username")
        val phone = data.getString("phone")
        val email = data.getString("email")

        this.applicationEventPublisher.publishEvent(
            PushNotifierEvent(
                s = this,
                eventId = eventId,
                username = username,
                subject = "Reminder: $subject",
                message = message
            )
        )

        if (phone != null && Validator.isValidPhoneNumber("BD", phone)) {
            logger.debug("Sending reminder to phone: $phone")
            try {
                smsService.sendSms(Providers.ADN_SMS, phone, "$subject: $message")
            } catch (e: Exception) {
                logger.error("Error sending reminder to phone: $phone", e)
            }
            logger.debug("Reminder sent to phone: $phone")
        }

        if (email != null && Validator.isValidEmail(email)) {
            logger.debug("Sending reminder to email: $email")
            mailService.send(
                email, subject, reminderTemplate(subject, message), true
            )
            logger.debug("Reminder sent to email: $email")
        }

    }

}