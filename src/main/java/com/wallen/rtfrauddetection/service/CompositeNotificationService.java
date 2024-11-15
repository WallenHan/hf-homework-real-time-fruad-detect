package com.wallen.rtfrauddetection.service;


import org.springframework.stereotype.Service;

import java.util.List;

@Service("compositeNotificationService")
public class CompositeNotificationService implements NotificationService {

    private final List<NotificationService> notificationServices;

    public CompositeNotificationService(List<NotificationService> notificationServices) {
        this.notificationServices = notificationServices;
    }

    @Override
    public void notify(String message) {
        for (NotificationService service : notificationServices) {
            service.notify(message);
        }
    }
}
