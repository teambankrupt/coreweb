package com.example.coreweb.domains.fileuploads.controllers;

import com.example.auth.config.security.SecurityContext;
import com.example.common.exceptions.invalid.ImageInvalidException;
import com.example.coreweb.domains.fileuploads.models.entities.UploadProperties;
import com.example.coreweb.domains.fileuploads.models.responses.ImageUploadResponse;
import com.example.coreweb.domains.fileuploads.services.FileUploadService;
import com.example.coreweb.utils.ImageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v2/images")
public class ImageControllerV2 {

    private final FileUploadService uploadService;

    @Value("${app.base-url-image}")
    private String baseUrlImages;

    @Autowired
    public ImageControllerV2(FileUploadService uploadService) {
        this.uploadService = uploadService;
    }

    // UPLOAD IMAGES
    @PostMapping("")
    public ResponseEntity uploadImage(@RequestParam("file") MultipartFile file,
                                      @RequestParam(value = "namespace") String namespace) throws ImageInvalidException, IOException {
        ImageUploadResponse response = upload(file, namespace, SecurityContext.getLoggedInUsername());
        return ResponseEntity.ok(response);
    }

    // UPLOAD IMAGES
    @PostMapping("/bulk")
    public ResponseEntity uploadImages(@RequestParam("files") MultipartFile[] files,
                                       @RequestParam(value = "namespace") String namespace) throws ImageInvalidException, IOException {
        if (files.length == 0) return ResponseEntity.badRequest().body("At least one image is expected!");
        List<ImageUploadResponse> responses = new ArrayList<>();
        for (MultipartFile file : files)
            responses.add(upload(file, namespace, SecurityContext.getLoggedInUsername()));

        return ResponseEntity.ok(responses);
    }


    private ImageUploadResponse upload(MultipartFile file, String namespace, String username) throws IOException, ImageInvalidException {
        if (!ImageValidator.isImageValid(file))
            throw new ImageInvalidException("Invalid Image");
        ImageUploadResponse response = new ImageUploadResponse();
        UploadProperties uploadProperties = this.uploadService.uploadFile(file, namespace, username, 1200);
        response.setImageUrl(this.baseUrlImages + uploadProperties.getImagePath());
        uploadProperties = this.uploadService.uploadFile(file, namespace, username + File.separator + "thumbs", 600);
        response.setThumbUrl(this.baseUrlImages + uploadProperties.getThumbPath());
        return response;
    }
}

