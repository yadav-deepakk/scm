package com.example.scm.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    // uploads image and returns the file path
    public String uploadImage(MultipartFile image, String filename);

    public Boolean deleteImage(String publicId);
}
