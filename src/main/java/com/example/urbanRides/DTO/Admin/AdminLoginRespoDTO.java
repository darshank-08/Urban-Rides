package com.example.urbanRides.DTO.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginRespoDTO {
    private String token;
    private String adminName;
    private String role;
}
