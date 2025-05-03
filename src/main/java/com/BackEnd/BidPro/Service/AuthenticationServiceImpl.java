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

import static com.BackEnd.BidPro.Util.VerificationTokenUtil.generateToken;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailSenderService emailSenderService;


    public AuthenticationResponse register(RegisterRequest request) {
        String token = generateToken();

        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .nationalId(request.getNationalid())
                .phoneNumber(request.getPhonenumber())
                .governorate(request.getGovernorate())
                .city(request.getCity())
                .address(request.getAddress())
                .balance(0L)
                .verificationToken(token)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER).build();

        repository.save(user);
        sendVerificationEmail(user);


        var jwtToken = jwtService.generateToken(user, user.getId(),user.getRole().name());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();


    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = repository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        var jwtToken = jwtService.generateToken(user, user.getId(),user.getRole().name());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public void sendVerificationEmail(User user) {
        String verificationLink = "This your verification mail: \n You should Click This Link to be verified \n http://localhost:8080/api/v1/auth/verify-email?token=" + user.getVerificationToken();
        emailSenderService.sendVerificationEmail(user.getEmail(), "Verification Email",verificationLink);
    }

}
