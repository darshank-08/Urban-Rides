package com.example.urbanRides.Controller.User;

import com.example.urbanRides.Entity.Booking;
import com.example.urbanRides.Entity.Delete;
import com.example.urbanRides.Entity.User;
import com.example.urbanRides.Repository.BookingRepository;
import com.example.urbanRides.Service.BookingService;
import com.example.urbanRides.Service.OwnerUserService;
import com.example.urbanRides.Service.RenterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Renter")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class RenterController {

    @Autowired
    private RenterUserService renterUserService;

    @Autowired
    OwnerUserService ownerUserService;

    @Autowired
    BookingService bookingService;

    @Autowired
    BookingRepository bookingRepository;

    // updating user
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody User req){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        ownerUserService.updateUser(userName, req);
        return ResponseEntity.ok("User Updated Successfully!");
    }

    // Active cars
    @GetMapping("/active-cars")
    public ResponseEntity<?> activeCars(){
        return renterUserService.activeCars();
    }

    @GetMapping("/car/{carID}")
    public ResponseEntity<?> CarDetails(@PathVariable String carID) {
        return renterUserService.CarDetails(carID);
    }


    @PostMapping("/booking/{carId}")
    public ResponseEntity<?> BookingCar(@PathVariable String carId,
                                        @RequestBody Booking req){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        return bookingService.bookCar(carId,req, userName);
    }

    @GetMapping("/my-Bookings")
    public ResponseEntity<?> Bookings(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        ResponseEntity<?> bookings = bookingService.myBookings(userName);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("my-Bookings/{id}")
    public ResponseEntity<?> getBooking(@PathVariable String id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        booking = bookingService.autoUpdateStatus(booking);

        return ResponseEntity.ok(booking);
    }


    @PutMapping("/cancel-booking/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable String id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        return renterUserService.cancelBooking(id);
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(@RequestBody Delete body) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        return renterUserService.deleteAccount(userName, body.getPassword());
    }
}
