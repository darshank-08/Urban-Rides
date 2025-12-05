package com.example.journalApp.Controller.User;

import com.example.journalApp.Entity.Car;
import com.example.journalApp.Entity.User;
import com.example.journalApp.Service.CarService;
import com.example.journalApp.Service.OwnerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Owner")
public class OwnerController {

    @Autowired
    OwnerUserService ownerUserService;

    @Autowired
    CarService carService;

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
    @GetMapping("/all-cars")
    public ResponseEntity<?> AllCars(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        User user = ownerUserService.findByUserName(userName);
        return ResponseEntity.ok(carService.myCars(user.getId()));
    }

    // updating car details by user
    @PutMapping("/update-car/{carId}")
    public ResponseEntity<?> updateCar(@PathVariable String carId, @RequestBody Car req){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        User user = ownerUserService.findByUserName(userName);
        Car updated = carService.updateCar(carId, req, user);
        return ResponseEntity.ok(updated);
    }

    // deleting cars by user
    @DeleteMapping("/delete-car/{carId}")
    public ResponseEntity<?> deleteCar(@PathVariable String carId){
        carService.deleteCar(carId);
        return ResponseEntity.ok("Car removed successfully!");
    }
}
