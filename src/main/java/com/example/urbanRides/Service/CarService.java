package com.example.urbanRides.Service;

import com.example.urbanRides.Entity.Booking;
import com.example.urbanRides.Entity.Car;
import com.example.urbanRides.Entity.User;
import com.example.urbanRides.Repository.BookingRepository;
import com.example.urbanRides.Repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BookingRepository  bookingRepository;

    // To add car
    public Car addCar(Car req, User user) {

        // Backend controlled data
        req.setOwnerId(user.getId());
        req.setOwnerName(user.getUserName());
        req.setStatus("PENDING_APPROVAL");
        req.setCreatedAt(LocalDateTime.now());

        return carRepository.save(req);
    }

    public List<Car> getCarsByOwnerId(String ownerId) {
        return carRepository.findByOwnerId(ownerId);
    }

    public ResponseEntity<?> deleteCar(String carId) {

        if (!carRepository.existsById(carId)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Car not found");
        }

        boolean hasActiveBooking =
                bookingRepository.existsByCarIdAndStatus(carId, "CONFIRMED");

        if (hasActiveBooking) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Car cannot be deleted. Active bookings exist.");
        }

        carRepository.deleteById(carId);
        return ResponseEntity.ok("Car deleted successfully");
    }


    // To update cars
    public Car updateCar(String carId, Car req) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        String User = auth.getName();

        Car existing = carRepository.findById(carId)
                .orElse(null);


        // Update only editable fields
        existing.setCompany(req.getCompany());
        existing.setModel(req.getModel());
        existing.setYear(req.getYear());
        existing.setImages(req.getImages());
        existing.setDocument(req.getDocument());
        existing.setPricePerDay(req.getPricePerDay());
        existing.setLocation(req.getLocation());
        existing.setSeats(req.getSeats());
        existing.setApproxMileage(req.getApproxMileage());
        existing.setCondition(req.getCondition());

        // When updated → send pending for review again
        existing.setStatus("PENDING_APPROVAL");

        return carRepository.save(existing);
    }

}

