package com.BackEnd.BidPro.cloudinary.service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    String uploadFile(MultipartFile file, String folderName) throws IOException;
}