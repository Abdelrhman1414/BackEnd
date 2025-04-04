package com.BackEnd.BidPro.Dto.Request;

import jakarta.validation.constraints.Email;
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

public class RegisterRequest {
    @Size(min = 3, message = "name must be at least 3 characters long")
    @NotBlank(message = "name is required")
    private String name;
    @Email(message = "Email is not valid")
    @NotBlank(message = "Email is required")
    private  String email;
//    @Size(min = 8, message = "password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
            message = "password must be at least 8 characters long, has at least one upper case , has at least one special character and has at least one digit")
    private  String password;
    @NotBlank(message = " confirm password is required")
    private String confirmpassword;
    @Size(min =14 ,max = 14, message = "length must be exactly 14 digits")
    @Pattern(regexp = "^[0-9]{14}$", message = "length must be exactly 14 digits")
    private String nationalid;
//    @Size(min =13 ,max = 13, message = "Phone number must be exactly 11 digits")
    @Pattern(regexp = "^\\+20(11|10|12|15)\\d{8}$", message = "Phone number must be exactly 11 digits")
    private  String phonenumber;
    @NotBlank(message = "governorate is required")
    private String governorate;
    @NotBlank(message = "city is required")
    private String city;
    @NotBlank(message = "address is required")
    private String address;

    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmpassword);
    }
}
