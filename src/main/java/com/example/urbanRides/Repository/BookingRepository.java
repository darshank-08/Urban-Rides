package com.example.urbanRides.Repository;

import com.example.urbanRides.Entity.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends MongoRepository <Booking, String>{
    List<Booking> findByCarIdAndStatus(String carID, String confirmed);

    List<Booking> findByRenterId(String id);

    boolean existsByCarIdAndStatus(String carId, String confirmed);

    Boolean existsByCarIdInAndStartDateGreaterThanEqualAndStatus(List<String> carIds, LocalDate today, String confirmed);
}
