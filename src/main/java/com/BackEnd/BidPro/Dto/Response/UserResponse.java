package com.BackEnd.BidPro.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String username;
    private  String email;
    private String nationalid;
    private  String phonenumber;
    private String state_region;
    private String city;
    private String address;
    private String image_url;
}
