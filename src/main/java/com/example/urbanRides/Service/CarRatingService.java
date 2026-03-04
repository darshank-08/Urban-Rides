package com.example.urbanRides.Service;

import com.example.urbanRides.DTO.Rating.CarRatingResponseDTO;
import com.example.urbanRides.Entity.Car;
import com.example.urbanRides.Entity.CarRating;
import com.example.urbanRides.Entity.User;
import com.example.urbanRides.Repository.CarRatingRepository;
import com.example.urbanRides.Repository.CarRepository;
import com.example.urbanRides.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarRatingService {

    private final CarRatingRepository ratingRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> submitRating(String bookingId,
                                                 String carId,
                                                 String userName,
                                                 int score) {

        if (score < 1 || score > 5) {
            return ResponseEntity.badRequest().body("Rating must be between 1 and 5");
        }

        if (ratingRepository.existsByBookingId(bookingId)) {
            return ResponseEntity.badRequest().body("Booking is already rated");
        }

        Optional<Car> carOptional = carRepository.findById(carId);
        if (carOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Car car = carOptional.get();

        if (car.getAverageRating() == null) {
            car.setAverageRating(0.0);
        }

        if (car.getTotalRatings() == null) {
            car.setTotalRatings(0);
        }

        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        CarRating rating = new CarRating();
        rating.setBookingId(bookingId);
        rating.setCarId(carId);
        rating.setUserId(user.getId());
        rating.setScore(score);
        rating.setCreatedAt(LocalDateTime.now());

        CarRating savedRating = ratingRepository.save(rating);

        double totalScore = car.getAverageRating() * car.getTotalRatings();
        int newTotalRatings = car.getTotalRatings() + 1;
        double newAverage =
                (totalScore + score) / newTotalRatings;

        car.setTotalRatings(newTotalRatings);
        car.setAverageRating(newAverage);

        carRepository.save(car);

        return ResponseEntity.ok(savedRating);
    }

}