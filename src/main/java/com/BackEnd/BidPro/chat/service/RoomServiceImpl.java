package com.BackEnd.BidPro.chat.service;

import com.BackEnd.BidPro.Model.User;
import com.BackEnd.BidPro.Repo.UserRepository;
import com.BackEnd.BidPro.chat.dto.request.CreateRoomRequest;
import com.BackEnd.BidPro.chat.dto.response.RoomResponse;
import com.BackEnd.BidPro.chat.model.Room;
import com.BackEnd.BidPro.chat.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    public void addRoom(CreateRoomRequest roomRequest) {
        List<Long> userIds = roomRequest.getUserIds();
        Room room = new Room();
        List<User> users = new ArrayList<>();
        StringBuilder nameOfRoom = new StringBuilder();
        for(Long userId : userIds) {
            User user = userRepository.findById(userId).get();
            users.add(user);
            nameOfRoom.append(user.getName()+",");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Please provide an valid Email!"));
        users.add(user);
        nameOfRoom.append("Admin, group chat");
        room.setName(nameOfRoom.toString());
        room.setUsers(users);
        room.setCreationDate(new Date());
        roomRepository.save(room);
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Please provide an valid Email!"));
        List<Room> rooms = roomRepository.findRoomsByUser(user.getId());
        List<RoomResponse> roomResponses = new ArrayList<>();
        for(Room room : rooms) {
            RoomResponse roomResponse = new RoomResponse();
            roomResponse.setId(room.getId());
            roomResponse.setName(room.getName());
            roomResponse.setCreationDate(room.getCreationDate());
            roomResponses.add(roomResponse);
        }
        return roomResponses;
    }
}
