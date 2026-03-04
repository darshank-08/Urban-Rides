package com.example.urbanRides.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "bookings")
@Data
@Getter
@Setter

public class Booking {
    @Id
    private String id;

    // by system
    private String carId;
    private String renterId;
    private double totalPrice;
    private LocalDateTime bookedAt;
    private String status;
    private String renter;
    private List<String> carImages;

    // by user (renter)
    private LocalDate startDate;
    private LocalDate endDate;
    private String paymentMethod;

}
