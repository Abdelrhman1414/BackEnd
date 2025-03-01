package com.BackEnd.BidPro.cloudinary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ImageResponse {
    private Long id;
    private String name;
    private String url;
}