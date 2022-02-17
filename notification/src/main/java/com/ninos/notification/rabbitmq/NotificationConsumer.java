package com.ninos.notification.rabbitmq;

import com.ninos.clients.notification.NotificationRequest;
import com.ninos.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationService notificationService;

    public void consumer(NotificationRequest notificationRequest){

    }


}
