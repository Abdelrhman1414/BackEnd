package com.BackEnd.BidPro.chat.controller;

import com.BackEnd.BidPro.chat.dto.request.CreateRoomRequest;
import com.BackEnd.BidPro.chat.model.Room;
import com.BackEnd.BidPro.chat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/admin/create")
    public ResponseEntity<?> createRoom(@RequestBody CreateRoomRequest roomRequest) {
        try {
            roomService.addRoom(roomRequest);
            return ResponseEntity.status(HttpStatus.OK).body("Room created");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/room/myrooms")
    public ResponseEntity<?> getMyRooms() {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(roomService.getAllRooms());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
