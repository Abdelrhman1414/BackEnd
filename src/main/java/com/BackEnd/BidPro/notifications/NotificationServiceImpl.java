package com.BackEnd.BidPro.notifications;

import com.BackEnd.BidPro.Model.User;
import com.BackEnd.BidPro.Repo.UserRepository;
import com.BackEnd.BidPro.notifications.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificaionService {
    private final SimpMessagingTemplate template;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public void sendNotification(String userId, Notification notification) {
        log.info("sending WS to {} with payload {}", userId, notification);
        template.convertAndSendToUser(
                userId,
                "/notification",
                notification
        );
    }

    @Override
    public List<NotificationResponse> userNotifications() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Please provide an valid Email!"));
        List<Notification> notifications = user.getNotifications();
        List<NotificationResponse> responses = new ArrayList<>();
        for(Notification notification : notifications) {
            NotificationResponse notificationResponse = new NotificationResponse();
            notificationResponse.setNotificationId(notification.getId());
            notificationResponse.setMessage(notification.getMessage());
            responses.add(notificationResponse);
        }
        return responses;
    }

    @Transactional
    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}
