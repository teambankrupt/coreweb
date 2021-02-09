package com.example.coreweb.domains.mail.services;

import com.example.common.utils.Validator;
import com.example.coreweb.domains.mail.models.entities.EmailLog;
import com.example.coreweb.domains.mail.repositories.EmailLogRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final EmailLogRepository emailLogRepository;

    @Autowired
    public MailServiceImpl(@Qualifier("javaMailSenderBean") JavaMailSender javaMailSender, EmailLogRepository emailLogRepository) {
        this.javaMailSender = javaMailSender;
        this.emailLogRepository = emailLogRepository;
    }

    @Override
    public boolean send(String to, String subject, String msgBody, boolean html) {
        return this.send(null, to, null, null, subject, msgBody, html, null);
    }

    @Override
    public boolean send(String to, String subject, String msgBody, boolean html, List<File> attachments) {
        return this.send(null, to, null, null, subject, msgBody, html, attachments);
    }

    @Override
    public boolean send(String from, String to, String[] cc, String[] bcc, String subject, String msgBody, boolean html, List<File> attachments) {
        cc = cc == null ? ArrayUtils.toArray() : cc;
        bcc = bcc == null ? ArrayUtils.toArray() : bcc;

        this.validateEmails(from, to, cc, bcc);
        if (subject == null || msgBody == null)
            throw new RuntimeException("Subject or Email body must not be null!");

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            if (from != null)
                helper.setFrom(from);
            helper.setTo(to);
            helper.setCc(cc);
            helper.setBcc(bcc);
            helper.setSubject(subject);
            helper.setText(msgBody, html);

            if (attachments != null)
                for (File a : attachments)
                    helper.addAttachment(a.getName(), new FileSystemResource(a));

            new Thread(() -> javaMailSender.send(message)).start();
        } catch (MessagingException e) {
            System.out.println(e.toString());
            return false;
        }
        this.saveLog(to, String.join(",", cc), String.join(",", cc), from, subject, msgBody, attachments == null ? 0 : attachments.size());
        return true;
    }

    private void validateEmails(String from, String to, String[] cc, String[] bcc) {

        boolean valid = Validator.isValidEmail(from);
        if (!valid) throw new RuntimeException("Invalid `from` email: " + from);

        valid = Validator.isValidEmail(to);
        if (!valid) throw new RuntimeException("Invalid `to` email: " + to);

        for (String email : cc) {
            valid = Validator.isValidEmail(email);
            if (!valid) throw new RuntimeException("Invalid `cc` email: " + email);
        }

        for (String email : bcc) {
            valid = Validator.isValidEmail(email);
            if (!valid) throw new RuntimeException("Invalid `bcc` email: " + email);
        }

    }

    private void saveLog(String to, String cc, String bcc, String from, String subject, String msg, int noOfAttachments) {
        EmailLog log = new EmailLog(from == null ? "SystemMail" : from, to, cc, bcc, subject, msg, noOfAttachments);
        this.emailLogRepository.save(log);
    }


}
