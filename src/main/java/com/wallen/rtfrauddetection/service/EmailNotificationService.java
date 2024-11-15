package com.wallen.rtfrauddetection.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("emailNotificationService")
@Slf4j
public class EmailNotificationService implements NotificationService {

    @Override
    public void notify(String message) {
        log.info("Fraudulent transaction is detected, {} ,mail is sending!", message);
    }
}
