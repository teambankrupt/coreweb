package com.example.coreweb.domains.mail.controllers

import com.example.coreweb.domains.mail.services.MailServiceV2
import com.example.coreweb.utils.FileIO
import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@Api(tags = ["Email Configs/Services"])
class MailControllerV2(
    private val mailService: MailServiceV2
) {

    @PostMapping("/api/v2/test-email")
    fun testEmailV2(
        @RequestParam(value = "from", required = false) from: String?,
        @RequestParam(value = "to") to: Array<String>,
        @RequestParam(value = "cc", required = false) cc: Array<String>?,
        @RequestParam(value = "bcc", required = false) bcc: Array<String>?,
        @RequestParam(value = "subject", defaultValue = "Test Email V2") subject: String,
        @RequestParam(value = "msg_body") msgBody: String,
        @RequestParam(value = "html", required = false) html: Boolean = false,
        @RequestParam(value = "files", required = false) attachments: Array<MultipartFile>?
    ) = this.mailService.send(
        from = from,
        to = to,
        cc = cc ?: emptyArray(),
        bcc = bcc ?: emptyArray(),
        subject = subject,
        body = msgBody,
        isHtml = html,
        attachments = (attachments ?: emptyArray<MultipartFile>())
            .map { FileIO.convertToFile(it) }
    )

}