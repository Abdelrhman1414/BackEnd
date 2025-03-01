package com.BackEnd.BidPro.cloudinary.service;


import com.BackEnd.BidPro.cloudinary.dto.ImageModel;
import com.BackEnd.BidPro.cloudinary.model.Image;
import com.BackEnd.BidPro.cloudinary.repo.ImageRepository;
import com.BackEnd.BidPro.Model.User;
import com.BackEnd.BidPro.Repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ResponseEntity<Map> uploadImage(Long id, ImageModel imageModel) {
        try {

            if (imageModel.getFile().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            User user = userRepository.findById(id).get();
            Image image = new Image();
            image.setUrl(cloudinaryService.uploadFile(imageModel.getFile(), "folder_1"));
            if (image.getUrl() == null) {
                return ResponseEntity.badRequest().build();
            }
            user.setImage(image);
            userRepository.save(user);
            return ResponseEntity.ok().body(Map.of("url", image.getUrl()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}