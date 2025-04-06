package com.BackEnd.BidPro.Service;

import com.BackEnd.BidPro.Dto.Request.AuthenticationRequest;
import com.BackEnd.BidPro.Dto.Request.RegisterRequest;
import com.BackEnd.BidPro.Dto.Response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
