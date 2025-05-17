package com.BackEnd.BidPro.notifications;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificaionService notificaionService;
    @GetMapping("/")
    public ResponseEntity<?> myNotifications() {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(notificaionService.userNotifications());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        try {
            notificaionService.deleteNotification(id);
            return ResponseEntity.status(HttpStatus.OK).body("Notification deleted");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
