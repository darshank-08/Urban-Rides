package com.example.journalApp.Controller.User;

import com.example.journalApp.Entity.Car;
import com.example.journalApp.Entity.User;
import com.example.journalApp.Repository.UserRepository;
import com.example.journalApp.Service.CarService;
import com.example.journalApp.Service.OwnerUserService;
import com.example.journalApp.Service.RenterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Renter")
public class RenterController {

    @Autowired
    private RenterUserService renterUserService;

    @Autowired
    OwnerUserService ownerUserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // updating user
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody User req){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        ownerUserService.updateUser(userName, req);
        return ResponseEntity.ok("User Updated Successfully!");
    }

    // Active cars
    @GetMapping("/active-cars")
    public ResponseEntity<?> activeCars(){
        return ResponseEntity.ok(renterUserService.activeCars());
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<?> carDetails(@PathVariable String carId){
        ResponseEntity<?> allCarDetails = renterUserService.CarDetails(carId);
        return ResponseEntity.ok(allCarDetails);
    }
}
