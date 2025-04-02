package com.BackEnd.BidPro.Service;

import com.BackEnd.BidPro.Dto.Response.ProductResponse;
import com.BackEnd.BidPro.Dto.Response.UserResponse;
import com.BackEnd.BidPro.Model.User;

import java.util.List;

public interface UserService {
    UserResponse details();
    List<ProductResponse> userAdvertisements();


}
