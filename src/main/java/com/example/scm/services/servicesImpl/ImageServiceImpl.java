package com.example.scm.services.servicesImpl;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.example.scm.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

    private Cloudinary cloudinary;

    public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile contactImage, String filename) {

        try {
            byte[] imageData = new byte[contactImage.getInputStream().available()];
            contactImage.getInputStream().read(imageData);
            cloudinary.uploader().upload(imageData, ObjectUtils.asMap("public_id", filename));

            return cloudinary
                    .url()
                    .transformation(new Transformation<>().width(500).height(500).crop("fill"))
                    .generate();

        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
