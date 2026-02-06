package com.example.urbanRides.Service;

import com.example.urbanRides.DTO.Employee.EmpSignupReqDTO;
import com.example.urbanRides.DTO.Employee.EmpSignupRespo;
import com.example.urbanRides.DTO.Employee.EmployeeLoginRespoDTO;
import com.example.urbanRides.Entity.Car;
import com.example.urbanRides.Entity.Employee;
import com.example.urbanRides.Entity.User;
import com.example.urbanRides.Repository.CarRepository;
import com.example.urbanRides.Repository.EmployeeRepository;
import com.example.urbanRides.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmployeeRepository employeeRepository;

    public ResponseEntity<?> registerEmp(EmpSignupReqDTO req) {

        Optional<Employee> existingEmployee =
                employeeRepository.findByEmpName(req.getUsername());

        if (existingEmployee.isPresent()) {
            return ResponseEntity.badRequest().body(
                    (EmpSignupReqDTO) Map.of(
                            "message", "Username is already taken",
                            "code", "Username taken"
                    )
            );
        }

        Employee employee = new Employee();

        // By user
        employee.setEmpName(req.getUsername());
        employee.setEmpPass(passwordEncoder.encode(req.getPassword()));
        employee.setEmpFullName(req.getFullName());
        employee.setEmpNumber(req.getPhoneNumber());
        employee.setGender(req.getGender());

        String email = req.getEmail();

        if (email == null || !email.endsWith("@gmail.com")) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "status", "FAILED",
                            "message", "Only Gmail addresses are allowed"
                    ));
        }
        employee.setEmail(req.getEmail());

        // By system
        employee.setRole("EMPLOYEE");
        employee.setStatus("ACTIVE");

        employeeRepository.save(employee);

        return ResponseEntity.ok("New profile Created successfully");
    }



    // Get all Approval pending Cars
    public List<Car> getPendingCars() {
        return carRepository.findByStatus("PENDING_APPROVAL");
    }

    // Approving Cars
    public ResponseEntity<?> CarApproval (String carId){
        Optional<Car> carOpt = carRepository.findById(carId);

        if (carOpt.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Car car = carOpt.get();

        if (!car.getStatus().equals("PENDING_APPROVAL")){
            return ResponseEntity.ok("Car is not pending for approval");
        }

        car.setStatus("ACTIVE");
        car.setApprovedAt(LocalDateTime.now());
        carRepository.save(car);
        return ResponseEntity.ok("Car Approved!");
    }

    // Rejecting Cars
    public ResponseEntity<?> CarRejection (String carId){
        Optional<Car> carOpt = carRepository.findById(carId);

        if (carOpt.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Car car = carOpt.get();

        if (car.getStatus().equals("REJECT")){
            return ResponseEntity.ok("Car is already rejected!");
        }

        car.setStatus("REJECT");
        car.setRejectedAt(LocalDateTime.now());
        carRepository.save(car);
        return ResponseEntity.ok("Car Rejected!");
    }

    // List of all Approved Cars
    public List<Car> activeCars(){
        return carRepository.findByStatus("ACTIVE");
    }

    // List of all Rejected Cars
    public List<Car> rejectedCars(){
        return carRepository.findByStatus("REJECT");
    }

    // Today Pending Cars
    public List<Car> getTodayPendingCars() {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        return carRepository.findByStatusAndCreatedAtAfter("PENDING_APPROVAL", startOfToday);
    }

    // Today Approved Cars
    public List<Car> getTodayApprovedCars() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        return carRepository.findByApprovedAtAfter(start);
    }

    // Today Rejected Cars
    public List<Car> getTodayRejectedCars() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        return carRepository.findByRejectedAtAfter(start);
    }

    public List<User> owners(){
        return userRepository.findByRolesContaining("OWNER");
    }

    public List<User> renters(){
        return userRepository.findByRolesContaining("RENTER");
    }

}
