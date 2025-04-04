package com.BackEnd.BidPro.Controller;

import com.BackEnd.BidPro.Dto.Request.RegisterRequest;
import com.BackEnd.BidPro.Dto.Request.UserRequest;
import com.BackEnd.BidPro.Repo.UserRepository;
import com.BackEnd.BidPro.Service.UserService;
import com.BackEnd.BidPro.cloudinary.dto.ImageModel;
import com.BackEnd.BidPro.cloudinary.service.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final ImageService imageService;
    private final UserService service;
    private final UserRepository repository;

    @GetMapping("/details")
    public ResponseEntity<?> details() {
        try {
            return ResponseEntity.ok(service.details());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/edit")
    public ResponseEntity<?> edit(@Valid @RequestBody UserRequest request, BindingResult bindingResult) {
        try {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            if (repository.findByPhone(request.getPhonenumber()).isPresent()) {
                errors.add("Phonenumber already in use");
            }

            if (!errors.isEmpty()) {
                return ResponseEntity.badRequest().body(errors);
            }

            service.edit(request);
            return ResponseEntity.ok("Edited Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());        }
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
    @GetMapping("/myads")
    public ResponseEntity<?> userAdvertisements() {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(service.userAdvertisements());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
