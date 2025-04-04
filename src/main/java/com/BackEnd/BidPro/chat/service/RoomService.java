package com.BackEnd.BidPro.chat.service;

import com.BackEnd.BidPro.chat.dto.request.CreateRoomRequest;
import com.BackEnd.BidPro.chat.dto.response.RoomResponse;
import com.BackEnd.BidPro.chat.model.Room;
import java.util.List;

public interface RoomService {
    void addRoom(CreateRoomRequest roomRequest);
    List<RoomResponse> getAllRooms();
}
