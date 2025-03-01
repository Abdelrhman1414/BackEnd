package com.BackEnd.BidPro.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RegisterRequest {
    private String username;
    private  String email;
    private  String password;
    private String nationalid;
    private  String phonenumber;
    private String state_region;
    private String city;
    private String address;
}
