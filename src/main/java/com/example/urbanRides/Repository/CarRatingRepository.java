package com.example.urbanRides.Repository;

import com.example.urbanRides.Entity.CarRating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRatingRepository extends MongoRepository<CarRating, String> {

    boolean existsByBookingId(String bookingId);

    List<CarRating> findByUserId(String userId);
}