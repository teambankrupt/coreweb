package com.example.coreweb.domains.sms.services.impl;

import com.example.common.models.DuplicateParamEntry;
import com.example.common.utils.NetworkUtil;
import com.example.coreweb.domains.sms.enums.Providers;
import com.example.coreweb.domains.sms.services.SmsService;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@PropertySource("classpath:sms.properties")
public class SmsServiceImpl implements SmsService {

    @Value("${mimsms.apiKey}")
    String apiKey;
    @Value("${mimsms.senderId}")
    String senderId;

    @Value("${twilio.auth.api-key}")
    String twilioApiKey;
    @Value("${twilio.auth.auth-token}")
    String twilioAuthToken;

    @Override
    public boolean sendSms(Providers provider, String phoneNumber, String message) {
        if (provider == Providers.MIM_SMS)
            return this.sendMimSms(phoneNumber, message);
        else if (provider == Providers.TWILIO)
            return this.sendTwilioSms(phoneNumber, message);

        return this.sendTwilioSms(phoneNumber, message);
    }

    public boolean sendMimSms(String phoneNumber, String message) {
        String phone = phoneNumber.trim().startsWith("88") ? phoneNumber : "88" + phoneNumber;
        String url = "https://bulk.mimsms.com/smsapi?api_key=" + this.apiKey + "&type=text&contacts=" + phone.trim() +
                "&senderid=" + this.senderId + "&msg=" + message;

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
                    requestBody
            );
            int status = response.getStatusLine().getStatusCode();
            return status >= 200 && status < 300;
        } catch (IOException e) {
            System.out.println("Could not send SMS. " + e.getMessage());
            return false;
        }
    }
}
