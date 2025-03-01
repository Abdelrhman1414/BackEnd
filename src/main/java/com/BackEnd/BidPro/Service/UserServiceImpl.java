package com.BackEnd.BidPro.Service;

import com.BackEnd.BidPro.Dto.Response.UserResponse;
import com.BackEnd.BidPro.Model.User;
import com.BackEnd.BidPro.Repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponse details() {
        String userEmail= SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()->new RuntimeException("Please provide an valid userName!"));
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
       if (user.getImage() == null){
            userResponse.setImage_url(null);
       }else {
           userResponse.setImage_url(user.getImage().getUrl());
        }
        userResponse.setNationalid(user.getNationalId());
        userResponse.setPhonenumber(user.getPhoneNumber());
        userResponse.setAddress(user.getAddress());
        userResponse.setCity(user.getCity());
        userResponse.setState_region(user.getState_region());

        return userResponse;
    }
}
