package com.example.coreweb.domains.mail.services;

import java.io.File;
import java.util.List;

public interface MailService {
    boolean send(String to, String subject, String msgBody, boolean html);
    boolean send(String[] to, String subject, String msgBody, boolean html);

    boolean send(String to, String subject, String msgBody, boolean html, List<File> attachments);
    boolean send(String[] to, String subject, String msgBody, boolean html, List<File> attachments);

    boolean send(String from, String[] to, String[] cc, String[] bcc, String subject, String msgBody, boolean html, List<File> attachments);
}
