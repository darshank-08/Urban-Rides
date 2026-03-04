package com.example.urbanRides.DTO.Rating;

import lombok.Data;

@Data
public class RatingRequest {
    private String bookingId;
    private String carId;
    private int score;
    private String userId;
}