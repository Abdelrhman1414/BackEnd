package com.BackEnd.BidPro.Controller;

import com.BackEnd.BidPro.Dto.Request.AuthenticationRequest;
import com.BackEnd.BidPro.Dto.Response.AuthenticationResponse;
import com.BackEnd.BidPro.Service.AuthenticationService;
import com.BackEnd.BidPro.Dto.Request.RegisterRequest;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.AuthenticationException;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor

public class AuthenticationController {

    private final AuthenticationService service;


    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request) {

        try {
            return ResponseEntity.ok(service.register(request));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }


    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request) {
   try{
        return ResponseEntity.ok(service.authenticate(request));

        }
     catch (AuthenticationException e) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
                          }
    }
}
