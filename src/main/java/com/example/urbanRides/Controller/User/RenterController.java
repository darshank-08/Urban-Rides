package com.example.urbanRides.Controller.User;

import com.example.urbanRides.DTO.Rating.RatingRequest;
import com.example.urbanRides.Entity.Booking;
import com.example.urbanRides.Entity.CarRating;
import com.example.urbanRides.Entity.Delete;
import com.example.urbanRides.Entity.User;
import com.example.urbanRides.Repository.BookingRepository;
import com.example.urbanRides.Repository.CarRatingRepository;
import com.example.urbanRides.Repository.UserRepository;
import com.example.urbanRides.Service.BookingService;
import com.example.urbanRides.Service.CarRatingService;
import com.example.urbanRides.Service.OwnerUserService;
import com.example.urbanRides.Service.RenterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    CarRatingService ratingService;

    @Autowired
    CarRatingRepository carRatingRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/user/{userName}")
    public ResponseEntity<?> user(@PathVariable String userName){
        return renterUserService.getUser(userName);
    }

    // updating user
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody User req){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        return ownerUserService.updateUser(userName, req);
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


    @PutMapping("/cancel-booking/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable String id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        return renterUserService.cancelBooking(id);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(@RequestBody Delete body) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        return renterUserService.deleteAccount(userName, body.getPassword());
    }

    @PostMapping("/submit-rating")
    public ResponseEntity<?> submitRating(@RequestBody RatingRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        return ratingService.submitRating(
                request.getBookingId(),
                request.getCarId(),
                userName,
                request.getScore()
        );
    }

    @GetMapping("/my-ratings")
    public ResponseEntity<?> getMyRatings() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userRepository.findByUserName(userName);


        List<CarRating> carRating = carRatingRepository.findByUserId(user.getId());

        if (carRating == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carRating);
    }
}
