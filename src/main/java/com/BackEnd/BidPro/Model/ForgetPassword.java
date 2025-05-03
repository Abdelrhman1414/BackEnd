package com.BackEnd.BidPro.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "forgetpasswords")

public class ForgetPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fpid;
    @Column(nullable = false)
    private String otp;
    @Column(nullable = false)
    private Date expirationTime;
    @OneToOne
    private User user;
}
