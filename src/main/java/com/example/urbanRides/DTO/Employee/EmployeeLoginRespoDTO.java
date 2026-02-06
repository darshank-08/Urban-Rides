package com.example.urbanRides.DTO.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLoginRespoDTO {

    private String token;
    private String username;
    private String role;
    private String message;

    public EmployeeLoginRespoDTO(String message, String role) {
        this.message = message;
        this.role = role;
    }

}
