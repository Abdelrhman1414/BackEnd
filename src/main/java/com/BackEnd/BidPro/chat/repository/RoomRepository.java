package com.BackEnd.BidPro.chat.repository;

import com.BackEnd.BidPro.chat.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT DISTINCT r FROM Room r JOIN FETCH r.users u WHERE u.id = :userId ORDER BY r.creationDate DESC ")
    List<Room> findRoomsByUser(@Param("userId") Long userId);
}
