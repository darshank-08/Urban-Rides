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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {

    @Autowired
    CarRepository carRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<?> bookCar(String carID, Booking req, String userName) {

        // 1. Fetch car
        Car car = carRepository.findById(carID)
                .orElse(null);

        if (car == null) {
            return ResponseEntity.badRequest().body("Car not found");
        }

        // 2. Fetch userID
        User renter = userRepository.findByUserName(userName);

        if (renter == null) {
            return ResponseEntity.badRequest().body("Please log in");
        }

        // 2. Validate dates
        LocalDate startDate = req.getStartDate();
        LocalDate endDate = req.getEndDate();

        if (startDate == null || endDate == null) {
            return ResponseEntity.badRequest().body("Start & end dates are required");
        }

        if (endDate.isBefore(startDate)) {
            return ResponseEntity.badRequest().body("End date cannot be before start date");
        }

        LocalDate today = LocalDate.now();

        if (startDate.isBefore(today)){
            return ResponseEntity.badRequest().body("Enter valid Start Date");
        }

        // 3. Overlap check
        List<Booking> existing = bookingRepository.findByCarIdAndStatus(carID, "CONFIRMED");

        for (Booking b : existing) {
            boolean overlap =
                    !startDate.isAfter(b.getEndDate()) &&
                            !endDate.isBefore(b.getStartDate());

            if (overlap) {
                return ResponseEntity.badRequest().body("Car already booked for these dates");
            }
        }

        // 4. Setting up price
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        double totalPrize = days * car.getPricePerDay();

        // creating fresh obj to avoid the unauthentic access to booking entity
        Booking booking = new Booking();

        booking.setCarId(carID);
        booking.setRenterId(renter.getId());
        booking.setTotalPrice(totalPrize);
        booking.setBookedAt(LocalDateTime.now());
        booking.setStatus("CONFIRMED");

        // User fields:
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setPaymentMethod(req.getPaymentMethod());

        bookingRepository.save(booking);

        Map<String, Object> bookingResponse = new LinkedHashMap<>();

        bookingResponse.put("bookingId", booking.getId());
        bookingResponse.put("carId", carID);

        // car block
        Map<String, Object> carInfo = new LinkedHashMap<>();
        carInfo.put("company", car.getCompany());
        carInfo.put("model", car.getModel());
        carInfo.put("year", car.getYear());
        bookingResponse.put("car", carInfo);

        // rentalPeriod block
        Map<String, Object> rentalPeriod = new LinkedHashMap<>();
        rentalPeriod.put("startDate", startDate);
        rentalPeriod.put("endDate", endDate);
        rentalPeriod.put("totalDays", days);
        bookingResponse.put("rentalPeriod", rentalPeriod);

        // pricing block
        Map<String, Object> pricing = new LinkedHashMap<>();
        pricing.put("pricePerDay", car.getPricePerDay());
        pricing.put("totalAmount", totalPrize);
        bookingResponse.put("pricing", pricing);

        // payment block
        Map<String, Object> payment = new LinkedHashMap<>();
        payment.put("method", booking.getPaymentMethod());
        payment.put("status", "PENDING");
        bookingResponse.put("payment", payment);

        // meta
        bookingResponse.put("status", booking.getStatus());
        bookingResponse.put("createdAt", booking.getBookedAt());

        // top-level response
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("message", "Car booked successfully");
        response.put("booking", bookingResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    public ResponseEntity<?> myBookings(String userName) {
        User renter = userRepository.findByUserName(userName);

        List<Booking> myBookings = bookingRepository.findByrenterId(renter.getId());

        if (myBookings.isEmpty()){
            return ResponseEntity.badRequest().body("No bookings!");
        }

        return ResponseEntity.ok(myBookings);

    }

    public Booking autoUpdateStatus(Booking booking) {
        LocalDate today = LocalDate.now();

        if (booking.getEndDate().isBefore(today)
                && booking.getStatus().equals("ACTIVE")) {

            booking.setStatus("COMPLETED");
            bookingRepository.save(booking);
        }

        return booking;
    }


}
