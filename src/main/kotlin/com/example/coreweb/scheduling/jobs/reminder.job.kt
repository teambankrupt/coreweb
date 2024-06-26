package com.example.coreweb.scheduling.jobs

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
import org.springframework.stereotype.Component

@Component
class ReminderJob(
    private val smsService: SmsService,
    private val mailService: MailService
) : Job {

    private val logger: Logger = LoggerFactory.getLogger(ReminderJob::class.java)

    override fun execute(jobContext: JobExecutionContext?) {
        val data: JobDataMap = jobContext?.mergedJobDataMap ?: return
        val subject = data.getString("subject")
        val message = data.getString("message")
        val phone = data.getString("phone")
        val email = data.getString("email")

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