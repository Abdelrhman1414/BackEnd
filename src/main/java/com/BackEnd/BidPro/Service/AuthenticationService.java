package com.BackEnd.BidPro.Service;

import com.BackEnd.BidPro.Config.JwtService;
import com.BackEnd.BidPro.Domain.Role;
import com.BackEnd.BidPro.Dto.Response.AuthenticationResponse;
import com.BackEnd.BidPro.Model.User;
import com.BackEnd.BidPro.Repo.UserRepository;
import com.BackEnd.BidPro.Dto.Request.AuthenticationRequest;
import com.BackEnd.BidPro.Dto.Request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private  final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {

            var user = User.builder()
                    .userName(request.getUsername())
                    .email(request.getEmail())
                    .nationalId(request.getNationalid())
                    .phoneNumber(request.getPhonenumber())
                    .state_region(request.getState_region())
                    .city(request.getCity())
                    .address(request.getAddress())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER).build();

                if(repository.findByEmail(request.getEmail()).isPresent()){
                    throw new UsernameNotFoundException("Email already in use");
                }
            repository.save(user);

            var jwtToken= jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();





    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = repository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        var jwtToken= jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
