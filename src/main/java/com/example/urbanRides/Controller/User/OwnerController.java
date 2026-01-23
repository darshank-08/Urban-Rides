package com.example.urbanRides.Controller.User;

import com.example.urbanRides.Entity.Car;
import com.example.urbanRides.Entity.User;
import com.example.urbanRides.Service.CarService;
import com.example.urbanRides.Service.ImageService;
import com.example.urbanRides.Service.OwnerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/Owner")
public class OwnerController {

    @Autowired
    OwnerUserService ownerUserService;

    @Autowired
    CarService carService;

    @Autowired
    ImageService imageService;

    // updating user
    @PutMapping("update")
    public ResponseEntity<?> update(@RequestBody User req){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        ownerUserService.updateUser(userName, req);
        return ResponseEntity.ok("User Updated Successfully!");
    }

    // deleting user
    @DeleteMapping("delete")
    public ResponseEntity<?> delete(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        ownerUserService.delete(userName);
        return ResponseEntity.ok("User Deleted Successfully!");
    }

    // Car adding req by user
    @PostMapping("/add-cars")
    public ResponseEntity<?> addCar(@RequestBody Car req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        User user = ownerUserService.findByUserName(userName);

        Car savedCar = carService.addCar(req, user);
        return ResponseEntity.ok(savedCar);
    }

    // all cars list by user
    @GetMapping("/cars")
    public ResponseEntity<List<Car>> getAllOwnerCars(Authentication authentication) {
        String username = authentication.getName();
        User owner = ownerUserService.findByUserName(username);

        List<Car> cars = carService.getCarsByOwnerId(owner.getId());
        return ResponseEntity.ok(cars);
    }

    // updating car details by user
    @PutMapping("/update-car/{carId}")
    public ResponseEntity<?> updateCar(@PathVariable String carId, @RequestBody Car req){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        User user = ownerUserService.findByUserName(userName);
        Car updated = carService.updateCar(carId, req);
        return ResponseEntity.ok(updated);
    }

    // deleting cars by user
    @DeleteMapping("/delete-car/{carId}")
    public ResponseEntity<?> deleteCar(@PathVariable String carId){
        return carService.deleteCar(carId);
    }

}
