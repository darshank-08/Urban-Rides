package com.example.journalApp.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "bookings")
@Data
public class Booking {
    @Id
    private String id;

    // by system
    private String carId;
    private String renterId;
    private double totalPrice;
    private LocalDateTime bookedAt;
    private String status;

    // by user (renter)
    private LocalDate startDate;
    private LocalDate endDate;
    private String paymentMethod;


}
