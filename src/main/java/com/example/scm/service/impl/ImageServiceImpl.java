package com.example.scm.service.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.example.scm.constants.AppConstants;
import com.example.scm.service.interfaces.ImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public @Service class ImageServiceImpl implements ImageService {

	private final Cloudinary cloudinary;

	@Override
	public String uploadImage(MultipartFile contactImage, String filename) {

		try {
			byte[] imageData = new byte[contactImage.getInputStream().available()];
			contactImage.getInputStream().read(imageData);
			cloudinary.uploader().upload(imageData, ObjectUtils.asMap("public_id", filename));

			String fileURL = cloudinary.url()
					.transformation(new Transformation<>().width(AppConstants.CONTACT_IMAGE_WIDTH)
							.height(AppConstants.CONTACT_IMAGE_HEIGHT).crop(AppConstants.CONTACT_IMAGE_CROP))
					.generate(filename);
			log.info("file:{} upload done, generated url is: {}", filename, fileURL);
			return fileURL;

		} catch (IOException ioe) {
			log.info("ImageServiceImpl || uploadImage | error: {}", ioe.getMessage(), ioe);
			return null;
		} catch (Exception e) {
			log.info("ImageServiceImpl || uploadImage | error: {}", e.getMessage(), e);
			return null;
		}
	}

	@Override
	public Boolean deleteImage(String publicId) {
		try {
			// delete
			@SuppressWarnings("unchecked")
			Map<String, String> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
			// Check result for success or failure
			if (result.get("result").equalsIgnoreCase("ok")) {
				log.info("Contact imageUUID deleted successfully.");
				return true;
			} else
				log.info("Error deleting resource:{}, cause:: {}", publicId, result.get("result"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
