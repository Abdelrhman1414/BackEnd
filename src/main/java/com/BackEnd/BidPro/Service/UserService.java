package com.BackEnd.BidPro.Service;

import com.BackEnd.BidPro.Dto.Request.UserRequest;
import com.BackEnd.BidPro.Dto.Response.ProductResponse;
import com.BackEnd.BidPro.Dto.Response.UserResponse;
import com.BackEnd.BidPro.Model.User;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    void deleteUser(Long id);

    UserResponse details();
    void         edit(UserRequest request);
    List<ProductResponse> userAdvertisements();


}
