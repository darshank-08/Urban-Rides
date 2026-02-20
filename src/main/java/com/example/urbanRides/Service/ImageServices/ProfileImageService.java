package com.example.urbanRides.Service.ImageServices;

import com.cloudinary.Cloudinary;
import com.example.urbanRides.Entity.Employee;
import com.example.urbanRides.Entity.User;
import com.example.urbanRides.Repository.EmployeeRepository;
import com.example.urbanRides.Repository.UserRepository;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@Service
public class ProfileImageService {

    private final Cloudinary cloudinary;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ProfileImageService(Cloudinary cloudinary, UserRepository userRepository, EmployeeRepository employeeRepository) {
        this.cloudinary = cloudinary;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    public String uploadProfileImage(MultipartFile file, String userName) {
        try {
            // 1️ Uploading to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of(
                            "folder", "urban_rides/profile_photos",
                            "resource_type", "image"
                    )
            );

            String imageUrl = uploadResult.get("secure_url").toString();

            User user = userRepository.findByUserName(userName);

            if (user == null){
                Optional<Employee> employeeOptional = employeeRepository.findByEmpName(userName);
                Employee employee = employeeOptional.get();

                employee.setProfileImageUrl(imageUrl);
                employeeRepository.save(employee);
            }

            // 3️ Saving image URL
            user.setProfileImageUrl(imageUrl);
            userRepository.save(user);

            return imageUrl;

        } catch (Exception e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }
}
