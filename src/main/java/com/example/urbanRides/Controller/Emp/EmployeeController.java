package com.example.urbanRides.Controller.Emp;

import com.example.urbanRides.Entity.Car;
import com.example.urbanRides.Entity.User;
import com.example.urbanRides.Service.EmployeeService;
import com.example.urbanRides.Service.OwnerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    OwnerUserService ownerUserService;

    //Get all Pending Cars
    @GetMapping("/pending-cars")
    public ResponseEntity<List<Car>> pendingCars() {
        return ResponseEntity.ok(employeeService.getPendingCars());
    }


    // Get all Active Cars
    @GetMapping("/active-cars")
    public ResponseEntity<?> ActiveCars(){
        List<Car> active = employeeService.activeCars();
        return ResponseEntity.ok(active);
    }

    // Get all Rejected Cars
    @GetMapping("/rejected-cars")
    public ResponseEntity<?> RejectedCars(){
        List<Car> rejected = employeeService.rejectedCars();
        return ResponseEntity.ok(rejected);
    }

    // Approving Cars
    @PutMapping("/car-approval/{carId}")
    public ResponseEntity<?> CarApproval(@PathVariable String carId){
        return employeeService.CarApproval(carId);
    }

    // Rejecting Cars
    @PutMapping("/car-reject/{carId}")
    public ResponseEntity<?> CarRejection(@PathVariable String carId){
        return employeeService.CarRejection(carId);
    }

    // Get Pending Cars Today
    @GetMapping("/pending-cars/today")
    public ResponseEntity<?> pendingCarsToday(){
        List<Car> pendingToday = employeeService.getTodayPendingCars();
        return ResponseEntity.ok(pendingToday);
    }

    // Get Approved Cars Today
    @GetMapping("/approved-cars/today")
    public ResponseEntity<?> approvedCarsToday(){
        List<Car> approvedToday = employeeService.getTodayApprovedCars();
        return ResponseEntity.ok(approvedToday);
    }

    // Get Rejected Cars Today
    @GetMapping("/rejected-cars/today")
    public ResponseEntity<?> rejectedCarsToday(){
        List<Car> rejectedToday = employeeService.getTodayRejectedCars();
        return ResponseEntity.ok(rejectedToday);
    }

    @GetMapping("/Get-Users")
    public ResponseEntity<?> GetUsers(){
        List<User> All = ownerUserService.getUsers();
        return ResponseEntity.ok(All);
    }

    @GetMapping("/Get-owners")
    public ResponseEntity<?> owners(){
        List<User> owners = employeeService.owners();
        return ResponseEntity.ok(owners);
    }

    @GetMapping("/Get-renters")
    public ResponseEntity<?> renters(){
        List<User> renters = employeeService.renters();
        return ResponseEntity.ok(renters);
    }

}
