package com.example.urbanRides.Controller.Upload;

import com.example.urbanRides.Service.ImageServices.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/Owner")
@CrossOrigin(origins = "http://localhost:5173")
public class CarPhotosController {

    private final ImageService imageService;

    public CarPhotosController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("image") MultipartFile file)
            throws IOException {
        String url = imageService.upload(file);
        return ResponseEntity.ok(url);
    }
}