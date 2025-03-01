package com.BackEnd.BidPro.Controller;

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

    @PostMapping("/image/{id}")
    public ResponseEntity<?> uploadingImage(@PathVariable Long id, ImageModel imageModel) {
        try {
            return new ResponseEntity<>(imageService.uploadImage(id,imageModel),HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
