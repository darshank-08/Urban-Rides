package com.example.urbanRides.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "car_ratings")
@Data
public class CarRating {


    @Id
    private String id;

    private String bookingId;
    private String carId;
    private String userId;

    private int score; // 1-5

    private LocalDateTime createdAt;

}