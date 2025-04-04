package com.BackEnd.BidPro.chat.repository;

import com.BackEnd.BidPro.chat.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.room.id = :roomId ORDER BY cm.messageDate ASC")
    List<ChatMessage> findSortedMessage(@Param("roomId") Long roomId);
}
