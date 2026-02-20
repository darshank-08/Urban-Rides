package com.example.urbanRides.Controller.Emp;

import com.example.urbanRides.Entity.Car;
import com.example.urbanRides.Entity.User;
import com.example.urbanRides.Service.BookingService;
import com.example.urbanRides.Service.EmployeeService;
import com.example.urbanRides.Service.OwnerUserService;
import com.example.urbanRides.Service.RenterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    OwnerUserService ownerUserService;

    @Autowired
    RenterUserService renterUserService;

    @GetMapping("/user")
    public ResponseEntity<?> getEmployee(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        return employeeService.getEmployee(userName);
    }

    //Get all Pending Cars
    @GetMapping("/pending-cars")
    public ResponseEntity<?> pendingCars() {
        return ResponseEntity.ok(employeeService.getPendingCars());
    }

    // Get all cars which are reviewed by ____
    @GetMapping("/Reviewed-By/{empName}")
    public ResponseEntity<?> reviewedBy(@PathVariable String empName){
        return employeeService.reviewedBy(empName);
    }

    // Approving Cars
    @PutMapping("/car-approval/{carId}/{empName}")
    public ResponseEntity<?> CarApproval(@PathVariable String carId,
                                         @PathVariable String empName){
        return employeeService.CarApproval(carId, empName);
    }

    // Rejecting Cars
    @PutMapping("/car-reject/{carId}/{empName}")
    public ResponseEntity<?> CarRejection(@PathVariable String carId,
                                          @PathVariable String empName){
        return employeeService.CarRejection(carId, empName);
    }

    @GetMapping("/Get-Users")
    public ResponseEntity<?> GetUsers(){
        List<User> All = ownerUserService.getUsers();
        return ResponseEntity.ok(All);
    }

    @GetMapping("/car/{carID}")
    public ResponseEntity<?> CarDetails(@PathVariable String carID) {
        return renterUserService.CarDetails(carID);
    }

    @GetMapping("/owner/{ownerName}")
    public  ResponseEntity<?> ownerDetails(@PathVariable String ownerName){
        return ownerUserService.getUser(ownerName);
    }
}
