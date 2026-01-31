package com.example.urbanRides.Controller.Upload;

import com.example.urbanRides.Service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/Owner")
@CrossOrigin(origins = "http://localhost:5173")
public class UploadController {

    private final ImageService imageService;

    public UploadController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("image") MultipartFile file)
            throws IOException {
        String url = imageService.upload(file);
        return ResponseEntity.ok(url);
    }
}