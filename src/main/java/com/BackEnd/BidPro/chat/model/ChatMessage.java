package com.BackEnd.BidPro.chat.model;

import com.BackEnd.BidPro.Model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "message_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date messageDate;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "room_id")
    private Room room;
}
