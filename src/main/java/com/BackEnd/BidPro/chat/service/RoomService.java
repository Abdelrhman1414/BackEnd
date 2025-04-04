package com.BackEnd.BidPro.chat.service;

import com.BackEnd.BidPro.chat.dto.request.CreateRoomRequest;
import com.BackEnd.BidPro.chat.model.Room;

public interface RoomService {
    void addRoom(CreateRoomRequest roomRequest);
}
