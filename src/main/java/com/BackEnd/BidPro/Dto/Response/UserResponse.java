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
    private Long id;
    private String name;
    private  String email;
    private String nationalid;
    private  String phonenumber;
    private String governorate;
    private String city;
    private String address;
    private String image_url;
}
