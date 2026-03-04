package com.example.urbanRides.DTO.Rating;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarRatingResponseDTO {
    private Double averageRating;
    private Integer totalRatings;
}
