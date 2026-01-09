package com.example.urbanRides.Service;

import com.example.urbanRides.Entity.Booking;
import com.example.urbanRides.Entity.Car;
import com.example.urbanRides.Entity.User;
import com.example.urbanRides.Repository.BookingRepository;
import com.example.urbanRides.Repository.CarRepository;
import com.example.urbanRides.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class RenterUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    // Register or Save User (Renter)
    public ResponseEntity<?> saveUser(User user) {
        String userName = user.getUserName();
        User exists = userRepository.findByUserName(userName);

        if (exists != null) {
            // Custom error response
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("code", "USERNAME_TAKEN"); // unique code
            errorBody.put("message", "UserName is already taken");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody); // 409 Conflict
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<String> defaultRoles = new ArrayList<>();
        defaultRoles.add("RENTER");
        user.setRoles(defaultRoles);
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    // Available cars for booking
    public ResponseEntity<?> activeCars(){
        List<Car> activeCars = carRepository.findByStatus("ACTIVE");

        if (activeCars.isEmpty()){
            return ResponseEntity.ok("No Cars Available");
        }

        return ResponseEntity.ok(activeCars);
    }

    // Car details with booked dates
    public ResponseEntity<?> CarDetails(String carID){
        Optional<Car> carDetailOps = carRepository.findById(carID);
        if (carDetailOps.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Car carDetails = carDetailOps.get();

        // Get bookings of this car that are ACTIVE / confirmed
        List<Booking> bookings = bookingRepository.findByCarIdAndStatus(carID, "CONFIRMED");

        // Extract booked dates (start and end) for frontend
        List<Map<String, LocalDate>> bookedDates = bookings.stream()
                .map(b -> Map.of(
                        "startDate", b.getStartDate(),
                        "endDate", b.getEndDate()
                ))
                .toList();

        // Custom response
        Map<String, Object> response = new HashMap<>();
        response.put("carDetails", carDetails);
        response.put("bookedDates", bookedDates);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> cancelBooking(String bookingId) {

        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);

        if (bookingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Booking booking = bookingOpt.get();

        LocalDate today = LocalDate.now();

        // If rental already started cannot cancel
        if (!today.isBefore(booking.getStartDate())) {
            return ResponseEntity.badRequest()
                    .body("You cannot cancel this booking because the rental period has already started.");
        }

        // ✔ Allowed → update status
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        return ResponseEntity.ok("Booking cancelled successfully.");
    }

    public ResponseEntity<?> deleteAccount(String username, String givenPassword) {

        User user = userRepository.findByUserName(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (!passwordEncoder.matches(givenPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body("Incorrect password");
        }

        List<Booking> bookings = bookingRepository.findByRenterId(user.getId());

        boolean hasActiveBooking = bookings.stream().anyMatch(b ->
                b.getStatus().equals("CONFIRMED") &&
                        !b.getEndDate().isBefore(LocalDate.now()) // block if booking is active or upcoming
        );

        if (hasActiveBooking) {
            return ResponseEntity.badRequest()
                    .body("You cannot delete account while car is currently rented or upcoming booking exists.");
        }

        userRepository.delete(user);

        return ResponseEntity.ok("Account deleted successfully");
    }



}
