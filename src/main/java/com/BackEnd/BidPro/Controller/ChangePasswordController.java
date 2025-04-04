package com.BackEnd.BidPro.Controller;

import com.BackEnd.BidPro.Dto.Request.ChangePasswordRequest;
import com.BackEnd.BidPro.Model.User;
import com.BackEnd.BidPro.Repo.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth/changePassword")
public class ChangePasswordController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest,
                                            Principal principal, BindingResult bindingResult) {
        try {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            String username = principal.getName();
            User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found: " + username));

//            if (user == null) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
//            }

            if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid current password");
                errors.add("Invalid current password");
            }
            if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password and confirm new password don't match");
                errors.add("New password and confirm new password don't match");
            }
            if (passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword()) && changePasswordRequest.getNewPassword().equals(changePasswordRequest.getCurrentPassword())) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You don't change your password");
                errors.add("You don't change your password");

            }
            if (!errors.isEmpty()) {
                return ResponseEntity.badRequest().body(errors);
            }

            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(user);
            return ResponseEntity.ok("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}



