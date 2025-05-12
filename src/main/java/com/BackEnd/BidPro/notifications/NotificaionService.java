package com.BackEnd.BidPro.notifications;

import com.BackEnd.BidPro.notifications.dto.NotificationResponse;
import java.util.List;

public interface NotificaionService {
    void sendNotification(String userId,Notification notification);
    List<NotificationResponse> userNotifications();

}
