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
        return this.send(new String[]{to}, subject, msgBody, html);
    }

    @Override
    public boolean send(String[] to, String subject, String msgBody, boolean html) {
        return this.send(null, to, null, null, subject, msgBody, html, null);
    }

    @Override
    public boolean send(String to, String subject, String msgBody, boolean html, List<File> attachments) {
        return this.send(new String[]{to}, subject, msgBody, html, attachments);
    }

    @Override
    public boolean send(String[] to, String subject, String msgBody, boolean html, List<File> attachments) {
        return this.send(null, to, null, null, subject, msgBody, html, attachments);
    }

    @Override
    public boolean send(String from, String[] to, String[] cc, String[] bcc, String subject, String msgBody, boolean html, List<File> attachments) {
        String[] toArr = to == null ? ArrayUtils.toArray() : to;
        String[] ccArr = cc == null ? ArrayUtils.toArray() : cc;
        String[] bccArr = bcc == null ? ArrayUtils.toArray() : bcc;

        this.validateEmails(from, to, cc, bcc);
        if (subject == null || msgBody == null)
            throw new RuntimeException("Subject or Email body must not be null!");
        new Thread(() -> {
            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                if (from != null)
                    helper.setFrom(from);
                helper.setTo(toArr);
                helper.setCc(ccArr);
                helper.setBcc(bccArr);
                helper.setSubject(subject);
                helper.setText(msgBody, html);

                if (attachments != null)
                    for (File a : attachments)
                        helper.addAttachment(a.getName(), new FileSystemResource(a));

                javaMailSender.send(message);
            } catch (MessagingException e) {
                System.out.println(e.toString());
            }
            this.saveLog(String.join(",", toArr), String.join(",", ccArr), String.join(",", bccArr), from, subject, msgBody, attachments == null ? 0 : attachments.size());
        }).start();

        return true;
    }


    private void saveLog(String to, String cc, String bcc, String from, String subject, String msg, int noOfAttachments) {
        EmailLog log = new EmailLog(from == null ? "SystemMail" : from, to, cc, bcc, subject, msg, noOfAttachments);
        this.emailLogRepository.save(log);
    }


    private void validateEmails(String from, String[] to, String[] cc, String[] bcc) {

        if (from != null && !from.isEmpty()) {
            boolean valid = Validator.isValidEmail(from);
            if (!valid) throw new RuntimeException("Invalid `from` email: " + from);
        }

        if (to != null)
            if (cc != null)
                Arrays.stream(to).forEach(m -> {
                    if (!Validator.isValidEmail(m)) throw new RuntimeException("Invalid `to` email: " + m);
                });

        if (cc != null)
            Arrays.stream(cc).forEach(m -> {
                if (!Validator.isValidEmail(m)) throw new RuntimeException("Invalid `cc` email: " + m);
            });

        if (bcc != null)
            Arrays.stream(bcc).forEach(m -> {
                if (!Validator.isValidEmail(m)) throw new RuntimeException("Invalid `bcc` email: " + m);
            });

    }

}
