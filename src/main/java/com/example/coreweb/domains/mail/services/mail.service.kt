package com.example.coreweb.domains.mail.services

import com.example.common.utils.Validator
import com.example.coreweb.domains.mail.models.entities.EmailLog
import com.example.coreweb.domains.mail.repositories.EmailLogRepository
import org.springframework.core.io.FileSystemResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.io.File
import java.nio.charset.StandardCharsets

interface MailServiceV2 {
    fun send(
        from: String? = null,
        to: Array<String>,
        cc: Array<String> = emptyArray(),
        bcc: Array<String> = emptyArray(),
        subject: String,
        body: String,
        isHtml: Boolean = false,
        attachments: List<File> = emptyList()
    )
}

@Service
class MailServiceBean(
    private val mailSender: JavaMailSender,
    private val emailLogRepository: EmailLogRepository
) : MailServiceV2 {
    override fun send(
        from: String?,
        to: Array<String>,
        cc: Array<String>,
        bcc: Array<String>,
        subject: String,
        body: String,
        isHtml: Boolean,
        attachments: List<File>
    ) = Thread {
        (to + cc + bcc).any { !Validator.isValidEmail(it) }.let {
            if (it) throw IllegalArgumentException("Invalid email address found")
        }
        val message = this.mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, StandardCharsets.UTF_8.name())
        from?.let { helper.setFrom(it) }
        helper.setTo(to)
        helper.setCc(cc)
        helper.setBcc(bcc)
        helper.setSubject(subject)
        helper.setText(body, isHtml)
        attachments.forEach { helper.addAttachment(it.name, FileSystemResource(it)) }
        mailSender.send(message).also {
            saveLog(to.joinToString(), cc.joinToString(), bcc.joinToString(), from, subject, body, attachments.size)
        }
    }.start()


    private fun saveLog(
        to: String,
        cc: String,
        bcc: String,
        from: String?,
        subject: String,
        msg: String,
        noOfAttachments: Int
    ) {
        val log = EmailLog(from ?: "SystemMail", to, cc, bcc, subject, msg, noOfAttachments)
        this.emailLogRepository.save(log)
    }
}
