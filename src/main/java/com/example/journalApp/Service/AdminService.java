package com.example.journalApp.Service;

import com.example.journalApp.Entity.Car;
import com.example.journalApp.Repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private CarRepository carRepository;

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

}
