package com.BackEnd.BidPro.chat.controller;

import com.BackEnd.BidPro.chat.dto.request.CreateRoomRequest;
import com.BackEnd.BidPro.chat.model.Room;
import com.BackEnd.BidPro.chat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestBody CreateRoomRequest roomRequest) {
        try {
            roomService.addRoom(roomRequest);
            return ResponseEntity.status(HttpStatus.OK).body("Room created");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
