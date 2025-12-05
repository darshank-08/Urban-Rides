package com.example.journalApp.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document("Cars")
public class Car {

    @Id
    private String id;

    // Auto Filled
    private String ownerId;
    private String ownerName;

    // User Inputs
    private String company;
    private String model;
    private Integer year;
    private List<String> images;
    private String document;
    private Double pricePerDay;
    private String location;
    private Integer seats;
    private Double approxMileage;
    private String condition;

    // System Controlled
    private String status; // PENDING_APPROVAL / ACTIVE / REJECTED
    private LocalDateTime createdAt;

    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;

}
