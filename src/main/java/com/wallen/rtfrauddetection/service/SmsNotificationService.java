package com.wallen.rtfrauddetection.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("smsNotificationService")
@Slf4j
public class SmsNotificationService implements NotificationService {

    @Override
    public void notify(String message) {
        log.info("Fraudulent transaction is detected, {} ,SMS is sending!", message);
    }
}
