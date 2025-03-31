package com.BackEnd.BidPro.notifications;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificaionServicee{
    private final SimpMessagingTemplate template;


    @Override
    public void sendNotification(String userId, Notification notification) {
        log.info("sending WS to {} with payload {}", userId, notification);
        template.convertAndSendToUser(
                userId,
                "/notification",
                notification
        );
    }
}
