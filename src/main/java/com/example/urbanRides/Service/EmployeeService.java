package com.example.urbanRides.Service;

import com.example.urbanRides.DTO.Employee.EmployeeSignReqDTO;
import com.example.urbanRides.DTO.Employee.EmployeeSignRespoDTO;
import com.example.urbanRides.Entity.Car;
import com.example.urbanRides.Entity.Admin;
import com.example.urbanRides.Entity.Employee;
import com.example.urbanRides.Entity.User;
import com.example.urbanRides.Repository.CarRepository;
import com.example.urbanRides.Repository.AdminRepository;
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


    public ResponseEntity<EmployeeSignRespoDTO> registerAdmin(EmployeeSignReqDTO req) {

        Optional<Employee> existingEmployee =
                employeeRepository.findByEmpName(req.getEmpName());

        if (existingEmployee.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new EmployeeSignRespoDTO(
                            "USERNAME_TAKEN",
                            "UserName is already taken"
                    ));
        }

        Employee employee = new Employee();
        employee.setEmpName(req.getEmpName());
        employee.setEmpPass(passwordEncoder.encode(req.getEmpPass()));
        employee.setEmpFullName(req.getEmpFullName());
        employee.setEmpNumber(req.getEmpNumber());
        employee.setGender(req.getGender());

        employee.setEmp(false);
        employee.setRole("PENDING");

        employeeRepository.save(employee);

        return ResponseEntity.ok(
                new EmployeeSignRespoDTO(
                        employee.getEmpName(),
                        employee.getRole()
                )
        );
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
