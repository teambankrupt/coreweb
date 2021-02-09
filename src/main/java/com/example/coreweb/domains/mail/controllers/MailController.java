package com.example.coreweb.domains.mail.controllers;


import com.example.coreweb.domains.mail.services.MailService;
import com.example.coreweb.utils.FileIO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@Api(tags = "Email Configs/Services")
public class MailController {
    private final MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/api/v1/test-email")
    private ResponseEntity<String> testEmail(
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to") String to,
            @RequestParam(value = "cc", required = false) String[] cc,
            @RequestParam(value = "bcc", required = false) String[] bcc,
            @RequestParam(value = "subject", defaultValue = "Test Email") String subject,
            @RequestParam(value = "msg_body", required = false) String msgBody,
            @RequestParam(value = "html") boolean html,
            @RequestParam(value = "files", required = false) MultipartFile[] attachments
    ) {
        this.mailService.send(
                from, to, cc, bcc,
                subject, msgBody, html,
                attachments == null ? null : Arrays.stream(attachments).map(multipartFile -> FileIO.convertToFile(multipartFile)).collect(Collectors.toList())
        );
        return ResponseEntity.ok("Email Sent!!");
    }
}
