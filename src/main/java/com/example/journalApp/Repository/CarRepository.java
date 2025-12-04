package com.example.journalApp.Repository;

import com.example.journalApp.Entity.Car;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CarRepository extends MongoRepository<Car, String> {
    List<Car> ownerId(String id);

    List<Car> findByStatus(String pendingApproval);

    List<Car> findByStatusAndCreatedAtAfter(String approved, LocalDateTime startOfToday);

    List<Car> findByApprovedAtAfter(LocalDateTime start);

    List<Car> findByRejectedAtAfter(LocalDateTime start);
}
