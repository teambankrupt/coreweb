package com.example.coreweb.domains.sms.services;

import com.example.coreweb.domains.sms.enums.Providers;

public interface SmsService {
    boolean sendSms(Providers provider, String phoneNumber, String message);
}
