package com.BackEnd.BidPro.chat.service;

import com.BackEnd.BidPro.Model.User;
import com.BackEnd.BidPro.Repo.UserRepository;
import com.BackEnd.BidPro.chat.dto.request.CreateRoomRequest;
import com.BackEnd.BidPro.chat.model.Room;
import com.BackEnd.BidPro.chat.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        for(Long userId : userIds) {
           users.add(userRepository.findById(userId).get());
        }
        room.setUsers(users);
        room.setCreationDate(new Date());
        roomRepository.save(room);
    }
}
