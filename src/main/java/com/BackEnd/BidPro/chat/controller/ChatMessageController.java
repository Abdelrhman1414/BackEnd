package com.BackEnd.BidPro.chat.controller;

import com.BackEnd.BidPro.chat.dto.request.MessageRequest;
import com.BackEnd.BidPro.chat.model.ChatMessage;
import com.BackEnd.BidPro.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @PostMapping("sendMessage")
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequest messageRequest) {
        try {
            chatMessageService.sendMessage(messageRequest);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getRoomMessages(@PathVariable Long roomId) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(chatMessageService.roomMessages(roomId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
