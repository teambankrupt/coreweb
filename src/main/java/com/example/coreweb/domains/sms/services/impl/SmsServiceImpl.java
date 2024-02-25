package com.example.coreweb.domains.sms.services.impl;

import com.example.common.models.DuplicateParamEntry;
import com.example.common.utils.NetworkUtil;
import com.example.coreweb.domains.mail.services.MailService;
import com.example.coreweb.domains.sms.enums.Providers;
import com.example.coreweb.domains.sms.services.SmsService;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@PropertySource("classpath:sms.properties")
public class SmsServiceImpl implements SmsService {

    @Value("${mimsms.apiKey}")
    String mimSmsApiKey;
    @Value("${mimsms.senderId}")
    String senderId;

    @Value("${adnsms.apiKey}")
    String adnSmsApiKey;

    @Value("${adnsms.apiSecret}")
    String adnSmsApiSecret;

    @Value("${twilio.auth.api-key}")
    String twilioApiKey;
    @Value("${twilio.auth.auth-token}")
    String twilioAuthToken;

    private final MailService mailService;

    @Autowired
    public SmsServiceImpl(
            MailService mailService
    ) {
        this.mailService = mailService;
    }

    @Override
    public boolean sendSms(Providers provider, String phoneNumber, String message) {
        try {
            if (provider == Providers.MIM_SMS)
                return this.sendMimSms(phoneNumber, message);
            else if (provider == Providers.TWILIO)
                return this.sendTwilioSms(phoneNumber, message);
            else if (provider == Providers.ADN_SMS)
                return this.sendAdnSms(phoneNumber, message);

            return this.sendTwilioSms(phoneNumber, message);
        } catch (Exception e) {
            this.mailService.send(
                    new String[]{"admin@servicito.com"},
                    "Error Sending SMS",
                    "Phone: " + phoneNumber + "<br/><br/>Message:<br/>" + message
                            + "<br/><br/>Error: " + e.getMessage(),
                    true
            );
            return false;
        }
    }

    private boolean sendAdnSms(String phoneNumber, String message) {
        String url = "https://portal.adnsms.com/api/v1/secure/send-sms";
        var params = new ArrayList<DuplicateParamEntry>();
        params.add(new DuplicateParamEntry("api_key", this.adnSmsApiKey));
        params.add(new DuplicateParamEntry("api_secret", this.adnSmsApiSecret));
        params.add(new DuplicateParamEntry("mobile", phoneNumber));
        params.add(new DuplicateParamEntry("message_body", message));
        params.add(new DuplicateParamEntry("message_type", "TEXT"));
        params.add(new DuplicateParamEntry("request_type", "SINGLE_SMS"));
        try {
            NetworkUtil.postFormData(url, null, params, ContentType.APPLICATION_FORM_URLENCODED);
            return true;
        } catch (IOException e) {
            System.out.println("Could not send SMS. " + e.getMessage());
        }
        return false;
    }

    public boolean sendMimSms(String phoneNumber, String message) {
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        String phone = phoneNumber.trim().startsWith("88") ? phoneNumber : "88" + phoneNumber;
        String url = "https://bulk.mimsms.com/smsapi?api_key=" + this.mimSmsApiKey + "&type=text&contacts=" + phone.trim() +
                "&senderid=" + this.senderId + "&msg=" + encodedMessage + "&label=transactional";

        try {
            NetworkUtil.postData(url, null, null, true);
            return true;
        } catch (IOException e) {
            System.out.println("Could not send SMS. " + e.getMessage());
            return false;
        }
    }

    public boolean sendTwilioSms(String phoneNumber, String message) {
        String phone = phoneNumber.startsWith("81") ? "+" + phoneNumber : phoneNumber;
        phone = phone.startsWith("+81") ? phone : "+81" + phone;

        String url = "https://api.twilio.com/2010-04-01/Accounts/ACf423009d245fb505abed5fc2f8e0fa40/Messages.json";
        List<DuplicateParamEntry> requestBody = new ArrayList<>();

        requestBody.add(new DuplicateParamEntry("From", "+16305568251"));
        requestBody.add(new DuplicateParamEntry("To", phone));
        requestBody.add(new DuplicateParamEntry("Body", message));

        try {
            String toEncode = this.twilioApiKey + ":" + this.twilioAuthToken;
            CloseableHttpResponse response = NetworkUtil.postFormData(
                    url,
                    "Basic " + Base64.encodeBase64String(toEncode.getBytes(StandardCharsets.UTF_8)),
                    requestBody,
                    ContentType.APPLICATION_FORM_URLENCODED
            );
            int status = response.getStatusLine().getStatusCode();
            return status >= 200 && status < 300;
        } catch (IOException e) {
            System.out.println("Could not send SMS. " + e.getMessage());
            return false;
        }
    }
}
