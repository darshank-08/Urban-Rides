package com.example.urbanRides.Repository;

import com.example.urbanRides.Entity.Car;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CarRepository extends MongoRepository<Car, String> {
    List<Car> findByStatus(String pendingApproval);

    List<Car> findByStatusAndCreatedAtAfter(String approved, LocalDateTime startOfToday);

    List<Car> findByApprovedAtAfter(LocalDateTime start);

    List<Car> findByRejectedAtAfter(LocalDateTime start);

    List<Car> findByOwnerIdAndStatus(String id, String active);

    List<Car> findByOwnerId(String ownerId);

    List<Car> findByreviewedBy(String employeeName);
}
