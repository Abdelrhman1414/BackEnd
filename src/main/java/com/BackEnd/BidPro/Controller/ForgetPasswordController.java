package com.BackEnd.BidPro.Controller;

import com.BackEnd.BidPro.Dto.Request.ResetPasswordRequest;
import com.BackEnd.BidPro.Model.ForgetPassword;
import com.BackEnd.BidPro.Model.User;
import com.BackEnd.BidPro.Repo.ForgetPasswordRepository;
import com.BackEnd.BidPro.Repo.UserRepository;
import com.BackEnd.BidPro.Service.EmailSenderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.BackEnd.BidPro.Util.OtpUtil.generateOtp;

@RestController
@RequestMapping("/forgetpassword")
@RequiredArgsConstructor
public class ForgetPasswordController {
    private final UserRepository repository;
    private final ForgetPasswordRepository forgetPasswordRepo;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/verifyEmail/{email}")
    public ResponseEntity<?> verifyEmail(@PathVariable String email) {
        try {
            User user = repository.findByEmail(email).orElseThrow(() -> new RuntimeException("Please provide an valid email!" + email));
            String otp = generateOtp();
            ForgetPassword fp = ForgetPassword.builder()
                    .otp(otp)
                    .expirationTime(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                    .user(user)
                    .build();
            forgetPasswordRepo.save(fp);
            emailSenderService.sendVerificationEmail(user.getEmail(),
                    "OTP For Forgot Password",
                    "This is The OTP Requested : " + otp);
            return ResponseEntity.ok("Email Sent For Verification");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Verify failed: " + e.getMessage());
        }
    }


        @PostMapping("/verifyOtp/{otp}/{email}")
        public ResponseEntity<?> verifyOtp(@PathVariable String otp, @PathVariable String email){
            try {
                User user = repository.findByEmail(email).orElseThrow(() -> new RuntimeException("Please provide an valid email!" + email));
                ForgetPassword fp = forgetPasswordRepo.findByOtpAndUser(otp, user)
                        .orElseThrow(() -> new RuntimeException("Invalid OTP for email: " + email));

                if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
                    forgetPasswordRepo.deleteById(fp.getFpid());
                    return new ResponseEntity<>("OTP has expired!", HttpStatus.EXPECTATION_FAILED);
                }
                return ResponseEntity.ok("OTP Verified");
            }catch (Exception e) {
                return ResponseEntity.badRequest().body("Verify failed: " + e.getMessage());
            }
        }

    @PostMapping("/resetPassword/{email}")
    public ResponseEntity<?> resetPasswordHandler( @Valid @RequestBody ResetPasswordRequest request
        , BindingResult bindingResult,    @PathVariable String email){
        try {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            if (!Objects.equals(request.getNewPassword(), request.getConfirmNewPassword())) {
                errors.add("Password and Confirm Password must match");
            }

            if (!errors.isEmpty()) {
                return ResponseEntity.badRequest().body(errors);
            }

            String passwordEncoded = passwordEncoder.encode(request.getNewPassword());

            repository.updatePassword(email, passwordEncoded);

            return ResponseEntity.ok("Password Has been changed");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Rest Password is failed: " + e.getMessage());
        }
    }

    }


