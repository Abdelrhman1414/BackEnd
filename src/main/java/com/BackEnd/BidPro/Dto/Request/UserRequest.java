package com.BackEnd.BidPro.Dto.Request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @Size(min = 3, message = "name must be at least 3 characters long")
    @NotBlank(message = "name is required")
    private String name;
//    @Size(min =13 ,max = 13, message = "Phone number must be exactly 11 digits")
    @Pattern(regexp = "^\\+20(11|10|12|15)\\d{8}$",message = "Phone number must be exactly 11 digits")
    private  String phonenumber;
    @NotBlank(message = "governorate is required")
    private String governorate;
    @NotBlank(message = "city is required")
    private String city;
    @NotBlank(message = "address is required")
    private String address;
}
