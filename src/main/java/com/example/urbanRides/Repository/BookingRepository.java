package com.example.urbanRides.Repository;

import com.example.urbanRides.Entity.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository <Booking, String>{
    List<Booking> findByCarIdAndStatus(String carID, String confirmed);

    List<Booking> findByrenterId(String id);

    List<Booking> findByRenterId(String id);
}
