package com.example.journalApp.Service;

import com.example.journalApp.Entity.Car;
import com.example.journalApp.Entity.User;
import com.example.journalApp.Repository.CarRepository;
import com.example.journalApp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RenterUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Available cars for booking
    public ResponseEntity<?> activeCars(){
        List<Car> activeCars = carRepository.findByStatus("ACTIVE");

        if (activeCars.isEmpty()){
            return ResponseEntity.ok("No Cars Available");
        }

        return ResponseEntity.ok(activeCars);
    }

    // car details
    public ResponseEntity<?> CarDetails(String carID){
        Optional<Car> carDetailOps = carRepository.findById(carID);

        Car carDetails = carDetailOps.get();
        return ResponseEntity.ok(carDetails);
    }
}
