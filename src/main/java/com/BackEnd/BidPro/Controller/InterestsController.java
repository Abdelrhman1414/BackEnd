package com.BackEnd.BidPro.Controller;

import com.BackEnd.BidPro.Dto.Request.InterestsRequest;
import com.BackEnd.BidPro.Service.InterestsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InterestsController {
    private final InterestsService interestsService;

    @PostMapping("/interests")
    public ResponseEntity<?> addInterests(@RequestBody InterestsRequest request){
        try {
            interestsService.addInterests(request);
            return ResponseEntity.status(HttpStatus.OK).body("Added Interests!");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/interests")
    public ResponseEntity<?> getAllInterests(){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(interestsService.findInterestCategory());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/foryou")
    public ResponseEntity<?> getForyouInterests(){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(interestsService.findProductInterests());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
