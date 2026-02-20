package com.example.urbanRides.Controller.Upload;

import com.example.urbanRides.Service.ImageServices.ProfileImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
public class ProfileImageController {

    private final ProfileImageService imageService;

    public ProfileImageController(ProfileImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload-profile-photo")
    public ResponseEntity<String> uploadProfilePhoto( @RequestParam("file") MultipartFile file,Principal principal) {
        String userName = principal.getName();

        String imageUrl = imageService.uploadProfileImage(file, userName);

        return ResponseEntity.ok(imageUrl);
    }
}
