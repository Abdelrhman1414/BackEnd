package com.BackEnd.BidPro.cloudinary.service;


import com.BackEnd.BidPro.cloudinary.dto.ImageModel;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ImageService {

    ResponseEntity<?> uploadImage(ImageModel imageModel);
    void deleteImage();
    String getImageUrl();
}