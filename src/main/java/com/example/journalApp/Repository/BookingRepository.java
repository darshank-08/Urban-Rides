package com.example.journalApp.Repository;

import com.example.journalApp.Entity.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends MongoRepository <Booking, String>{
    List<Booking> findByCarIdAndStatus(String carID, String confirmed);

    List<Booking> findByrenterId(String id);

    List<Booking> findByRenterId(String id);
}
