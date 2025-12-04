package com.example.journalApp.Controller.Admin;

import com.example.journalApp.Entity.Car;
import com.example.journalApp.Service.AdminService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    // Get all Approval pending Cars
    @GetMapping("/pending-cars")
    public ResponseEntity<?> pendingCars(){
        List<Car> pending = adminService.getPendingCars();
        return ResponseEntity.ok(pending);
    }

    // Get all Active Cars
    @GetMapping("/active-cars")
    public ResponseEntity<?> ActiveCars(){
        List<Car> active = adminService.activeCars();
        return ResponseEntity.ok(active);
    }

    // Get all Rejected Cars
    @GetMapping("/rejected-cars")
    public ResponseEntity<?> RejectedCars(){
        List<Car> rejected = adminService.rejectedCars();
        return ResponseEntity.ok(rejected);
    }

    // Approving Cars
    @PutMapping("/car-approval/{carId}")
    public ResponseEntity<?> CarApproval(@PathVariable String carId){
        return adminService.CarApproval(carId);
    }

    // Rejecting Cars
    @PutMapping("/car-reject/{carId}")
    public ResponseEntity<?> CarRejection(@PathVariable String carId){
        return adminService.CarRejection(carId);
    }

    // Get Pending Cars Today
    @GetMapping("/pending-cars/today")
    public ResponseEntity<?> pendingCarsToday(){
        List<Car> pendingToday = adminService.getTodayPendingCars();
        return ResponseEntity.ok(pendingToday);
    }

    // Get Approved Cars Today
    @GetMapping("/approved-cars/today")
    public ResponseEntity<?> approvedCarsToday(){
        List<Car> approvedToday = adminService.getTodayApprovedCars();
        return ResponseEntity.ok(approvedToday);
    }

    // Get Rejected Cars Today
    @GetMapping("/rejected-cars/today")
    public ResponseEntity<?> rejectedCarsToday(){
        List<Car> rejectedToday = adminService.getTodayRejectedCars();
        return ResponseEntity.ok(rejectedToday);
    }

}
