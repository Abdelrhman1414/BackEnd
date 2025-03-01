package com.BackEnd.BidPro.Controller;

import com.BackEnd.BidPro.Service.UserService;
import com.BackEnd.BidPro.cloudinary.dto.ImageModel;
import com.BackEnd.BidPro.cloudinary.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final ImageService imageService;
    private final UserService service;
    @GetMapping("/details")
    public ResponseEntity<?> details() {
        try {
            return ResponseEntity.ok(service.details());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/image")
    public ResponseEntity<?> uploadingImage( ImageModel imageModel) {
        try {
            return new ResponseEntity<>(imageService.uploadImage(imageModel),HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
