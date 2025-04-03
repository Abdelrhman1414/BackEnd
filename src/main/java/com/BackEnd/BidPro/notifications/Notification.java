package com.BackEnd.BidPro.notifications;

import com.BackEnd.BidPro.Model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name="notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name="id")
    private Long id;

    @Column(name="message")
    private String message;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;


}
