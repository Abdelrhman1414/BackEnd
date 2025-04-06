package com.BackEnd.BidPro.Controller;

import com.BackEnd.BidPro.Dto.Request.AuthenticationRequest;
import com.BackEnd.BidPro.Dto.Response.AuthenticationResponse;
import com.BackEnd.BidPro.Repo.UserRepository;
import com.BackEnd.BidPro.Service.AuthenticationService;
import com.BackEnd.BidPro.Dto.Request.RegisterRequest;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.AuthenticationException;import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;import org.springframework.context.support.DefaultMessageSourceResolvable;



@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor

public class AuthenticationController {

    private final AuthenticationService service;
    private final UserRepository repository;



    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request,  BindingResult bindingResult) {

        try {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            if (!request.isPasswordMatching()) {
                errors.add("Password and Confirm Password must match");
            }
            if (repository.findByEmail(request.getEmail()).isPresent()) {
                errors.add("Email already in use");
            }
            if (repository.findByPhone(request.getPhonenumber()).isPresent()) {
                errors.add("Phonenumber already in use");
            }
            if (repository.findByNationalId(request.getNationalid()).isPresent()) {
                errors.add("Nationalid already in use");
            }

            if (!errors.isEmpty()) {
                return ResponseEntity.badRequest().body(errors);
            }
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
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect email or password");
                          }
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
