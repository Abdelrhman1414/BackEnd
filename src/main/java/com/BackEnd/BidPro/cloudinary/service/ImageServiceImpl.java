package com.BackEnd.BidPro.cloudinary.service;


import com.BackEnd.BidPro.cloudinary.dto.ImageModel;
import com.BackEnd.BidPro.cloudinary.model.Image;
import com.BackEnd.BidPro.cloudinary.repo.ImageRepository;
import com.BackEnd.BidPro.Model.User;
import com.BackEnd.BidPro.Repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Override
    public ResponseEntity<Map> uploadImage(ImageModel imageModel) {
        try {

            if (imageModel.getFile().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Please provide an valid userName!"));
            Image image = new Image();
            image.setUrl(cloudinaryService.uploadFile(imageModel.getFile(), "folder_1"));
            if(user.getImage()!=null){
                imageRepository.delete(user.getImage());
                if (image.getUrl() == null) {
                    return ResponseEntity.badRequest().build();
                }
                user.setImage(image);
                userRepository.save(user);
                return ResponseEntity.ok().body(Map.of("url", image.getUrl()));
            }
            else {
                if (image.getUrl() == null) {
                    return ResponseEntity.badRequest().build();
                }
                user.setImage(image);
                userRepository.save(user);
                return ResponseEntity.ok().body(Map.of("url", image.getUrl()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteImage() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Please provide an valid userName!"));
        Image image =  user.getImage();
       imageRepository.deleteById(image.getId());
       user.setImage(null);
       userRepository.save(user);
    }
}

